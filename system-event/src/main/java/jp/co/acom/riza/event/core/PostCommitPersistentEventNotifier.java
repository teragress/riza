package jp.co.acom.riza.event.core;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import jp.co.acom.riza.cep.CepMonitorService;
import jp.co.acom.riza.context.CommonContext;
import jp.co.acom.riza.event.entity.TranEventEntity;
import jp.co.acom.riza.event.entity.TranEventEntityKey;
import jp.co.acom.riza.event.kafka.KafkaEventProducer;
import jp.co.acom.riza.event.msg.EntityManagerPersistent;
import jp.co.acom.riza.event.msg.EntityPersistent;
import jp.co.acom.riza.event.msg.EventHeader;
import jp.co.acom.riza.event.msg.FlowEvent;
import jp.co.acom.riza.event.repository.TranEventEntityRepository;
import jp.co.acom.riza.event.utils.JsonConverter;
import jp.co.acom.riza.utils.log.Logger;
import jp.com.acom.renove.event.mq.MessageUtilImpl;

/** 中のイベントを Commit 後に送信する. */
@Component
@Scope(value = "transaction", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PostCommitPersistentEventNotifier {
	private static final Logger logger = Logger.getLogger(PostCommitPersistentEventNotifier.class);

	private List<PersistentEventHolder> eventHolders = new ArrayList<>();
	private FlowEvent flowEvent;
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

	@PostConstruct
	public void initialize() {
		TransactionSynchronizationManager.registerSynchronization(new TransactionListener());
	}

	/**
	 * イベントを保持しているオブジェクトを追加する.
	 *
	 * @param holder イベント保持オブジェクト
	 */
	public void addEventHolder(PersistentEventHolder holder) {
		eventHolders.add(holder);
	}

	/**
	 * フローイベント作成
	* @return
	*/
	private FlowEvent createFlowEvent() {
		flowEvent = new FlowEvent();
		flowEvent.setFlowId(commonContext.getFlowid());
		EventHeader eventHeader = new EventHeader();
		eventHeader.setReqeustId(commonContext.getReqeustId());
		eventHeader.setUserId(commonContext.getUserId());
		flowEvent.setEventHeader(eventHeader);
		List<EntityManagerPersistent> empList = new ArrayList<EntityManagerPersistent>();
		for (PersistentEventHolder pHolder : eventHolders) {
			EntityManagerPersistent emp = new EntityManagerPersistent();
			emp.setEntityManagerName(pHolder.getEntityManagerBeanName());
			List<EntityPersistent> ePersistents = new ArrayList<EntityPersistent>();
			for (PersistentEvent pEvent : pHolder.getEvents()) {
				EntityPersistent ep = new EntityPersistent();
				ep.setEntityClassName(pEvent.getEntity().getClass().getName());
				ep.setKeyClassName(pEvent.getEntityId().getClass().getName());
				ep.setEntityType(pEvent.getEntityType());
				ep.setKeyObject(pEvent.getEntityId());
				ep.setPersitenceEventType(pEvent.getPersistentType());
				ep.setRevision(pHolder.getRevision());
				ePersistents.add(ep);
			}
			empList.add(emp);
		}
		flowEvent.setEntityManagerPersistences(empList);
		return flowEvent;
	}

	/**
	 * Kafkaパーシステントイベントメッセージ送信<br>
	 * MQメッセージ送信<br>
	 * @author mtera1003
	 *
	 */
	private class TransactionListener extends TransactionSynchronizationAdapter {
		@Override
		public void afterCommit() {
			kafkaProducer.sendEventMessage(flowEvent);
			try {
				messageUtil.flush();
			} catch (NamingException e) {

				logger.error("Mq message send Exception occurred.",e);
			}
			monitor.endMonitor(commonContext.getTraceId() + commonContext.getSpanId());

			super.afterCommit();
		}

		/**
		 * コミット前のインターセプター<br>
		 * イベントテーブルの挿入<br>
		 * CEP監視開始
		 */
		@Override
		public void beforeCommit(boolean readOnly) {
			insertTranEvent();
			monitor.startMonitor(commonContext.getTraceId() + commonContext.getSpanId(), commonContext.getDate());
			//cep監視リクエスト
			super.beforeCommit(readOnly);
		}

		/**
		 * トランザクションイベントテーブル挿入
		 */
		private void insertTranEvent() {
			flowEvent = createFlowEvent();
			TranEventEntity tranEntity = new TranEventEntity();
			tranEntity.setEventMsg(JsonConverter.objectToJsonString(flowEvent));
			TranEventEntityKey tranKey = new TranEventEntityKey();
			tranKey.setTranId(commonContext.getTraceId() + commonContext.getSpanId());
			tranKey.setDate(commonContext.getDate());
			tranEntity.setTranEventKey(tranKey);
			tranEventRepository.save(tranEntity);
		}


		/**
		 *　CEP監視終了(ロールバック含む)
		 */
		@Override
		public void afterCompletion(int status) {
			monitor.endMonitor(commonContext.getTraceId() + commonContext.getSpanId());
			super.afterCompletion(status);
		}
	}
}
