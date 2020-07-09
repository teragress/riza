package jp.co.acom.riza.event.core;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jp.co.acom.riza.cep.CepMonitorService;
import jp.co.acom.riza.context.CommonContext;
import jp.co.acom.riza.event.core.PersistentHolder.AuditStatus;
import jp.co.acom.riza.event.entity.EventCheckpointEntity;
import jp.co.acom.riza.event.entity.EventCheckpointEntityKey;
import jp.co.acom.riza.event.kafka.KafkaEventProducer;
import jp.co.acom.riza.event.mq.MessageUtilImpl;
import jp.co.acom.riza.event.msg.Header;
import jp.co.acom.riza.event.msg.TranEvent;
import jp.co.acom.riza.event.msg.Entity;
import jp.co.acom.riza.event.msg.Manager;
import jp.co.acom.riza.event.repository.TranEventEntityRepository;
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.system.CommonConstants;
import jp.co.acom.riza.system.utils.log.Logger;
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
	 * フローイベント
	 */
	private TranEvent tranEvent;
	/**
	 * CommonContext
	 */
	@Autowired
	private CommonContext commonContext;

	@Autowired
	private TranEventEntityRepository tranEventRepository;

	@Autowired
	private CepMonitorService monitor;

	@Autowired
	KafkaEventProducer kafkaProducer;

	@Autowired
	MessageUtilImpl messageUtil;

	@Autowired
	PostCommitPersistentNotifier eventNotifier;

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
		sepKey = commonContext.getTraceId() + commonContext.getSpanId();
		monitor.startMonitor(sepKey, commonContext.getLjcomDateTime());
	}

	/**
	 * トランザクションイベントテーブル挿入
	 */
	private void insertTranEvent() {
		logger.info("insertTranEvent() started.");
		tranEvent = createTranEvent();
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
			logger.info("afterCommit() started.");
			kafkaProducer.sendEventMessage(tranEvent);
			try {
				messageUtil.flush();
			} catch (NamingException e) {

				logger.error("Mq message send Exception occurred.", e);
			}
			monitor.endMonitor(commonContext.getTraceId() + commonContext.getSpanId());

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
				if (holder.getAuditStatus() != AuditStatus.INIT) {
					allInit = false;
				}
			}
			if (allInit) {
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
			monitor.endMonitor(sepKey);
			super.afterCompletion(status);
		}
	}
}
