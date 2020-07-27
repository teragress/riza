package jp.co.acom.riza.event.cmd;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.cmd.parm.EventRecoveryParm;
import jp.co.acom.riza.event.config.EventConfiguration;
import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.event.entity.EventCheckpointEntity;
import jp.co.acom.riza.event.entity.EventCheckpointEntityKey;
import jp.co.acom.riza.event.kafka.KafkaEventProducer;
import jp.co.acom.riza.event.kafka.KafkaEventUtil;
import jp.co.acom.riza.event.msg.TranEvent;
import jp.co.acom.riza.event.repository.EventCheckPointEntityRepository;
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.exception.EventCommandException;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

/**
 * チェックポイントテーブルからイベントのリカバリーを行う
 * 
 * @author teratani
 *
 */
@Service
public class EventRecovery {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(EventRecovery.class);

	private static final int QUERY_MAX_SIZE = 100;
//	private static final int QUERY_MAX_SIZE = 2;

	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	EventCheckPointEntityRepository checkPointRepository;

	@Autowired
	KafkaEventUtil kafkaEventUtil;

	@Autowired
	KafkaEventProducer kafkaEventProducer;

	/**
	 * 個別キー指定のリカバリー処理
	 * @param parm コマンドパラメータ
	 * @throws Exception
	 */
	public void keyRecovery(EventRecoveryParm parm) throws Exception {
		logger.debug("keyRecovery() started."); 

		EventCheckpointEntityKey key = new EventCheckpointEntityKey();
		key.setTranId(parm.getTranid());
		key.setDatetime(parm.getDateTime());
		EntityManager em = (EntityManager)applicationContext.getBean(EventConfiguration.ENTITY_MANAGER_NAME);
		EventCheckpointEntity entity = em.find(EventCheckpointEntity.class,key);

		if (entity != null) {
			String eventMessage = mergeEventMessage(entity, em);
			TranEvent tranEvent = StringUtil.stringToTranEventEventObject(eventMessage);
			kafkaEventUtil.resendMqMessage(tranEvent.getTopicMessages(), tranEvent.getMessageIdPrefix(), true);
			kafkaEventProducer.sendEventMessage(tranEvent);
			logger.info(MessageFormat.get(EventMessageId.EVENT_RECOVERY_EXECUTE), tranEvent.toString());
			em.detach(entity);
		} else {
			throw new EventCommandException(MessageFormat.get(EventMessageId.RECORD_NOT_FOUND));
		}
	}

	/**
	 * 日時範囲指定でのリカバリー処理
	 * @param parm コマンドパラメータ
	 * @throws Exception
	 */
	public void rangeRecovery(EventRecoveryParm parm) throws Exception {
		logger.debug("rangeRecovery() started."); 

		String fromDateTime = parm.getDateTime();
		String toDateTime;
		if (parm.getToDateTime() == null) {
			toDateTime = "99999999999999";
		} else {
			toDateTime = parm.getToDateTime();
		}
		String tranId = "";
		EntityManager em = (EntityManager)applicationContext.getBean(EventConfiguration.ENTITY_MANAGER_NAME);

		logger.debug("fromTimestamp(" + fromDateTime + ") toTimestamp(" + toDateTime + ")");
		
		List<EventCheckpointEntity> checkpointList = em
				.createNamedQuery(EventCheckpointEntity.FIND_BY_DATETIME_FIRST, EventCheckpointEntity.class)
				.setParameter("fromDateTime", fromDateTime).setParameter("toDateTime", toDateTime)
				.setMaxResults(QUERY_MAX_SIZE).getResultList();

		while (checkpointList != null && checkpointList.size() != 0) {
			for (EventCheckpointEntity checkpoint : checkpointList) {
				logger.debug("checkpoint="+checkpoint.toString());
				String eventMessage = mergeEventMessage(checkpoint, em);
				TranEvent tranEvent = StringUtil.stringToTranEventEventObject(eventMessage);
				kafkaEventUtil.resendMqMessage(tranEvent.getTopicMessages(), tranEvent.getMessageIdPrefix(), false);
				kafkaEventProducer.sendEventMessage(tranEvent);
				em.detach(checkpoint);
				fromDateTime = checkpoint.getTranEventKey().getDatetime();
				tranId = checkpoint.getTranEventKey().getTranId();
				logger.info(MessageFormat.get(EventMessageId.EVENT_RECOVERY_EXECUTE), tranEvent.toString());
			}

			if (checkpointList.size() > 0) {
				EventCheckpointEntity lastEntity = checkpointList.get(QUERY_MAX_SIZE - 1);

				logger.debug("lastckpoint="+ lastEntity.toString());
				logger.debug("fromTimestamp(" + fromDateTime + ") toTimestamp(" + toDateTime + ")");
				checkpointList = em
						.createNamedQuery(EventCheckpointEntity.FIND_BY_DATETIME_NEXT, EventCheckpointEntity.class)
						.setParameter("fromDateTimeTranId", fromDateTime + tranId)
						.setParameter("toDateTime", toDateTime)
						.setMaxResults(QUERY_MAX_SIZE).getResultList();
			} else {
				checkpointList = null;
			}
		}
	}
	
	/**
	 * チェックポイントテーブルのレコードが分割された場合のマージ処理
	 * @param checkpoint イベントチェックポイントエンティティ
	 * @param em エンティティマネージャー
	 * @return イベントメッセージ
	 */
	private String mergeEventMessage(EventCheckpointEntity checkpoint,EntityManager em) {
		if (checkpoint.getCnt() == 1) {
			return checkpoint.getEventMsg();
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(checkpoint.getEventMsg());
		for (int i=1; i<checkpoint.getCnt();i++) {
			EventCheckpointEntityKey key = new EventCheckpointEntityKey();
			key.setDatetime(checkpoint.getTranEventKey().getDatetime());
			key.setTranId(checkpoint.getTranEventKey().getTranId());
			key.setSeq(i);
			EventCheckpointEntity entity = em.find(EventCheckpointEntity.class, key);
			if (entity == null) {
				throw new EventCommandException("EventCheckpointEntity not found key=" + key.toString());
			}
			stringBuilder.append(entity.getEventMsg());
			em.detach(checkpoint);
		}
		return stringBuilder.toString();
	}
}
