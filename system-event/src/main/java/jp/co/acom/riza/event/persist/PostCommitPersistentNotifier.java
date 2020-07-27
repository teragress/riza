package jp.co.acom.riza.event.persist;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jp.co.acom.riza.context.CommonContext;
import jp.co.acom.riza.event.cep.CepMonitorService;
import jp.co.acom.riza.event.config.EventConstants;
import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.event.entity.EventCheckpointEntity;
import jp.co.acom.riza.event.entity.EventCheckpointEntityKey;
import jp.co.acom.riza.event.kafka.KafkaEventProducer;
import jp.co.acom.riza.event.kafka.KafkaEventUtil;
import jp.co.acom.riza.event.kafka.MessageUtil;
import jp.co.acom.riza.event.kafka.MessageHolderUtil;
import jp.co.acom.riza.event.msg.Header;
import jp.co.acom.riza.event.msg.KafkaMessage;
import jp.co.acom.riza.event.msg.KafkaTopicMessage;
import jp.co.acom.riza.event.msg.TranEvent;
import jp.co.acom.riza.event.persist.PersistentHolder.AuditStatus;
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

/**
 * トランザクション単位のインタセプター関連を制御する
 * 
 * @author teratani
 *
 */
@Getter
@Setter
@Service
@Scope(value = CommonConstants.TRANSACTION_SCOPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PostCommitPersistentNotifier {
	private static final Logger logger = Logger.getLogger(PostCommitPersistentNotifier.class);

	/**
	 * チェックポイントのトランザクションメッセージ分割サイズ 
	 */
	private Integer TranMessageSplitSize;
	
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
	 * パーシステントイベント有無
	 */
	private boolean persistentEvent = false;
	
	/**
	 * MQ用KAFKA退避メッセージ情報
	 */
	private List<KafkaMessage> kafkaMessages = new ArrayList<KafkaMessage>();

	@Autowired
	Environment env;

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
	MessageHolderUtil messageHolderUtil;

	@Autowired
	KafkaEventUtil kafkaEventUtil;

	@Autowired
	PostCommitPersistentNotifier eventNotifier;

	private String sepKey;

	/**
	 * 初期化処理として環境変数の取り込みとトランザクションリスナーの登録
	 */
	@PostConstruct
	public void initialize() {
		logger.info("initialize() started.");
		TranMessageSplitSize = env.getProperty(EventConstants.CHECK_POINT_SPLIT_SIZE, Integer.class,
				EventConstants.DEFAULT_CHECK_POINT_SPLIT_SIZE);
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
	 * トランザクションイベント作成
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

		// tranEvent.setMqCount(messageUtil.getMessageCount());

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

	/**
	 * コミット前のイベント処理
	 */
	public void beforeEvent() {

		if (tranEvent == null && persistentEvent) {
			insertTranEvent();
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
			List<KafkaTopicMessage> topicMessages;
			byte[] messagePrefix = MessageUtil.getUniqueID();
			topicMessages = kafkaEventUtil.saveMqMessage(messagePrefix);

			tranEvent = createTranEvent();
			tranEvent.setMessageIdPrefix(messagePrefix);
			tranEvent.setTopicMessages(topicMessages);

			EventCheckpointEntity tranEntity = new EventCheckpointEntity();

			String evMsg = StringUtil.objectToJsonString(tranEvent);
			List<String> splitStr = StringUtil.splitByLength(evMsg, TranMessageSplitSize);

			for (int i = 0; i < splitStr.size(); i++) {
				EventCheckpointEntityKey tranKey = new EventCheckpointEntityKey();
				tranKey.setTranId(commonContext.getTraceId() + commonContext.getSpanId());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				tranKey.setDatetime(commonContext.getLjcomDateTime().format(formatter));
				tranKey.setSeq(i);
				tranEntity.setTranEventKey(tranKey);
				tranEntity.setCnt(splitStr.size());
				tranEntity.setEventMsg(splitStr.get(i));
				logger.info("******* TranEntity=" + tranEntity);
				tranEventRepository.save(tranEntity);
			}
		} catch (Exception ex) {
			logger.error(MessageFormat.get(EventMessageId.EVENT_EXCEPTION), ex.getMessage());
			logger.error(MessageFormat.get(EventMessageId.EXCEPTION_INFORMATION), ex);
			Integer forceNullException = null;
			forceNullException.intValue();
		}
	}

	/**
	 * Kafkaパーシステントイベントメッセージ送信<br>
	 * MQメッセージ送信<br>
	 * 
	 * @author teratani
	 *
	 */
	private class TransactionListener extends TransactionSynchronizationAdapter {

		@Override
		public void afterCommit() {
			logger.debug("afterCommit() started.");
			if (auditMessage.getAuditEntity().size() > 0) {
				auditMessage.setUser(commonContext.getUserId());
				logger.info(MessageFormat.get("RIZA0001"), StringUtil.objectToJsonString(auditMessage));
			}

			if (!persistentEvent) {
				super.afterCommit();
				return;
			}
			kafkaProducer.sendEventMessage(tranEvent);

			try {
				messageHolderUtil.flush(tranEvent.getMessageIdPrefix());
			} catch (Exception e) {

				logger.error("Mq message send Exception occurred.", e);
			}

			monitor.endMonitor(commonContext.getTraceId() + commonContext.getSpanId(),commonContext.getLjcomDateTime());

			super.afterCommit();
		}

		/**
		 * コミット前のインターセプター<br>
		 * イベントテーブルの挿入<br>
		 * CEP監視開始
		 */
		@Override
		public void beforeCommit(boolean readOnly) {
			logger.info("beforCommit() started. readOnly=" + readOnly);

			if (holders.size() == 0 && persistentEvent) {
				insertTranEvent();
				for (PersistentHolder holder : holders) {
					holder.setAuditStatus(AuditStatus.COMPLETE);
				}
				sepKey = commonContext.getTraceId() + commonContext.getSpanId();
				monitor.startMonitor(sepKey, commonContext.getLjcomDateTime());
			}
			super.beforeCommit(readOnly);
		}
	}
}
