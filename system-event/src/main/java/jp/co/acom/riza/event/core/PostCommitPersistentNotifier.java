package jp.co.acom.riza.event.core;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.xml.soap.MessageFactory;

import org.jgroups.protocols.tom.MessageID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jp.co.acom.riza.cep.CepMonitorService;
import jp.co.acom.riza.context.CommonContext;
import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.event.core.PersistentHolder.AuditStatus;
import jp.co.acom.riza.event.entity.EventCheckpointEntity;
import jp.co.acom.riza.event.entity.EventCheckpointEntityKey;
import jp.co.acom.riza.event.kafka.KafkaEventProducer;
import jp.co.acom.riza.event.kafka.MessageUtil;
import jp.co.acom.riza.event.kafka.MessageUtilImpl;
import jp.co.acom.riza.event.msg.Header;
import jp.co.acom.riza.event.msg.KafkaMessage;
import jp.co.acom.riza.event.msg.KafkaTopicMessage;
import jp.co.acom.riza.event.msg.TranEvent;
import jp.co.acom.riza.event.msg.AuditEntity;
import jp.co.acom.riza.event.msg.AuditMessage;
import jp.co.acom.riza.event.msg.Entity;
import jp.co.acom.riza.event.msg.Manager;
import jp.co.acom.riza.event.repository.EventCheckPointEntityRepository;
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.system.CommonConstants;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;
import lombok.Getter;
import lombok.Setter;

/** 中のイベントを Commit 後に送信する. */
@Getter
@Setter
@Service
@Scope(value = CommonConstants.TRANSACTION_SCOPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PostCommitPersistentNotifier {
	private static final Logger logger = Logger.getLogger(PostCommitPersistentNotifier.class);

	private static final int TRAN_MESSAGE_MAX_SIZE = 24000;
	/**
	 * エンティティマネージャー単位のホルダー
	 */
	private List<PersistentHolder> holders = new ArrayList<>();
	/**
	 * 監査メッセージホルダー
	 */
	private AuditMessage auditMessage = new AuditMessage();
	/**
	 * チェクポイント用トランザクションイベント
	 */
	private TranEvent tranEvent;

	/**
	 * 
	 */
	private List<KafkaMessage> kafkaMessages = new ArrayList<KafkaMessage>();
	/**
	 * CommonContext
	 */
	@Autowired
	private CommonContext commonContext;

	@Autowired
	private EventCheckPointEntityRepository tranEventRepository;

	@Autowired
	private CepMonitorService monitor;

	@Autowired
	KafkaEventProducer kafkaProducer;

	@Autowired
	MessageUtilImpl messageUtil;

	@Autowired
	PostCommitPersistentNotifier eventNotifier;

	@Autowired
	MessageUtilImpl msgUtil;

	private String sepKey;

	@PostConstruct
	public void initialize() {
		logger.info("initialize() started.");
		TransactionSynchronizationManager.registerSynchronization(new TransactionListener());
	}

	/**
	 * イベントを保持しているオブジェクトを追加する.
	 *
	 * @param holder イベント保持オブジェクト
	 */
	public void addEventHolder(PersistentHolder holder) {
		logger.info("addEventHolder() started. holder=" + holder);
		holders.add(holder);
	}

	/**
	 * フローイベント作成
	 * 
	 * @return
	 */
	private TranEvent createTranEvent() {
		logger.info("createTranEvent() started.");

		tranEvent = new TranEvent();
		tranEvent.setBusinessProcess(commonContext.getBusinessProcess());

		Header eventHeader = new Header();
		eventHeader.setReqeustId(commonContext.getReqeustId());
		eventHeader.setUserId(commonContext.getUserId());
		tranEvent.setHeader(eventHeader);

		tranEvent.setMqCount(msgUtil.getMessageCount());

		List<Manager> managerList = new ArrayList<Manager>();
		for (PersistentHolder holder : holders) {
			Manager pManager = new Manager();
			pManager.setManager(holder.getEntityManagerBeanName());
			List<Entity> pList = new ArrayList<Entity>();
			pManager.setEntitys(pList);
			pManager.setRevison(holder.getRevision());
			for (EntityPersistent eP : holder.getEvents()) {
				Entity pEntity = new Entity();
				pEntity.setEntity(eP.getEntity().getClass().getName());
				pEntity.setEntityType(eP.getEntityType());
				pEntity.setKey(eP.getEntityId());
				pEntity.setType(eP.getPersistentType());
				pList.add(pEntity);
			}
			managerList.add(pManager);
		}
		tranEvent.setManagers(managerList);
		return tranEvent;
	}

	public void beforeEvent() {

		insertTranEvent();
		if (tranEvent != null && tranEvent.getManagers().size() > 0) {
			sepKey = commonContext.getTraceId() + commonContext.getSpanId();
			monitor.startMonitor(sepKey, commonContext.getLjcomDateTime());
		}
	}

	/**
	 * トランザクションイベントテーブル挿入
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@SuppressWarnings("null")
	private void insertTranEvent() {
		logger.info("insertTranEvent() started.");

		try {
			tranEvent = createTranEvent();
			tranEvent.setMessageIdPrefix(MessageUtil.getUniqueID());

			List<KafkaTopicMessage> topicMessages = new ArrayList<KafkaTopicMessage>();
			if (messageUtil.getMessageCount() > 0) {
				topicMessages = messageUtil.saveReportMessage(tranEvent.getMessageIdPrefix());
			}
			tranEvent.setTopicMessages(topicMessages);

			if (tranEvent != null && tranEvent.getManagers().size() > 0) {

				EventCheckpointEntity tranEntity = new EventCheckpointEntity();

				String evMsg = StringUtil.objectToJsonString(tranEvent);
				List<String> splitStr = StringUtil.splitByLength(evMsg, TRAN_MESSAGE_MAX_SIZE);

				for (int i = 0; i < splitStr.size(); i++) {
					EventCheckpointEntityKey tranKey = new EventCheckpointEntityKey();
					tranKey.setTranId(commonContext.getTraceId() + commonContext.getSpanId());
					tranKey.setDatetime(Timestamp.valueOf(commonContext.getLjcomDateTime()));
					tranKey.setSeq(i);
					tranEntity.setTranEventKey(tranKey);
					tranEntity.setCnt(splitStr.size());
					tranEntity.setEventMsg(splitStr.get(i));
					logger.info("******* TranEntity=" + tranEntity);
					tranEventRepository.save(tranEntity);
				}
			}
		} catch (Exception ex) {
			logger.error(MessageFormat.get(EventMessageId.EVENT_EXCEPTION),ex.getMessage());
			logger.error(MessageFormat.get(EventMessageId.EXCEPTION_INFORMATION),ex);
			Integer forceNullException = null;
			forceNullException.intValue();
		}
	}

	/**
	 * Kafkaパーシステントイベントメッセージ送信<br>
	 * MQメッセージ送信<br>
	 * 
	 * @author mtera1003
	 *
	 */
	private class TransactionListener extends TransactionSynchronizationAdapter {

		@Override
		public void afterCommit() {
			logger.info("*****************************************afterCommit() started.");
			if (auditMessage.getAuditEntity().size() > 0) {
				auditMessage.setUser(commonContext.getUserId());
				logger.info(MessageFormat.get("RIZA0001"), StringUtil.objectToJsonString(auditMessage));
			}

			if (tranEvent != null && tranEvent.getManagers().size() > 0) {
				kafkaProducer.sendEventMessage(tranEvent);
			}
			try {
				messageUtil.flush(tranEvent.getMessageIdPrefix());
			} catch (Exception e) {

				logger.error("Mq message send Exception occurred.", e);
			}
			if (tranEvent != null && tranEvent.getManagers().size() > 0) {
				monitor.endMonitor(commonContext.getTraceId() + commonContext.getSpanId());
			}

			super.afterCommit();
		}

//		@Override
//		public int getOrder() {
//			return Ordered.LOWEST_PRECEDENCE;
//		}

		/**
		 * コミット前のインターセプター<br>
		 * イベントテーブルの挿入<br>
		 * CEP監視開始
		 */
		@Override
		public void beforeCommit(boolean readOnly) {
			logger.info("beforCommit() started. readOnly=" + readOnly);
			boolean allInit = true;
			for (PersistentHolder holder : holders) {
				if (holder.getAuditStatus() != AuditStatus.INIT && holder.getAuditStatus() != AuditStatus.COMPLETE) {
					allInit = false;
				}
			}
			if (allInit && (holders.size() > 0 || messageUtil.getMessageCount() > 0)) {
				insertTranEvent();
				sepKey = commonContext.getTraceId() + commonContext.getSpanId();
				for (PersistentHolder holder : holders) {
					holder.setAuditStatus(AuditStatus.COMPLETE);
				}
			}
			super.beforeCommit(readOnly);
		}

		@Override
		public void beforeCompletion() {
			logger.info("beforeCompletion() started.");
			super.beforeCompletion();
		}

		/**
		 * CEP監視終了(ロールバック含む)
		 */
		@Override
		public void afterCompletion(int status) {
			logger.info("afterCompletion() started. status=" + status);
			// cep監視終了リクエスト
			if (tranEvent != null && tranEvent.getManagers().size() > 0) {
				monitor.endMonitor(sepKey);
			}

			super.afterCompletion(status);
		}
	}
}
