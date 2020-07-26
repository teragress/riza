package jp.co.acom.riza.event.cmd;

import java.util.List;
import java.util.Optional;

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
import jp.co.acom.riza.event.kafka.KafkaUtil;
import jp.co.acom.riza.event.msg.TranEvent;
import jp.co.acom.riza.event.repository.EventCheckPointEntityRepository;
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.exception.EventCommandException;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

/**
 * @author vagrant
 *
 */
@Service
public class EventRecovery {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(EventRecovery.class);

//	private static final int QUERY_MAX_SIZE = 100;
	private static final int QUERY_MAX_SIZE = 3;

	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	EventCheckPointEntityRepository checkPointRepository;

	@Autowired
	KafkaUtil kafkaUtil;

	@Autowired
	KafkaEventProducer kafkaEventProducer;

	/**
	 * @param parm
	 * @throws Exception
	 */
	public void keyRecovery(EventRecoveryParm parm) throws Exception {

		EventCheckpointEntityKey key = new EventCheckpointEntityKey();
		key.setTranId(parm.getTranid());
		key.setDatetime(parm.getDateTime());
		Optional<EventCheckpointEntity> entity = checkPointRepository.findById(key);
		if (entity.isPresent()) {
			TranEvent tranEvent = StringUtil.stringToTranEventEventObject(entity.get().getEventMsg());
			kafkaUtil.resendMqMessage(tranEvent.getTopicMessages(), tranEvent.getMessageIdPrefix(), true);
			kafkaEventProducer.sendEventMessage(tranEvent);
		} else {
			throw new EventCommandException(MessageFormat.get(EventMessageId.RECORD_NOT_FOUND));
		}
	}

	/**
	 * @param parm
	 * @throws Exception
	 */
	public void rangeRecovery(EventRecoveryParm parm) throws Exception {

		String fromDateTime = parm.getDateTime();
		String toDateTime;
		if (parm.getToDateTime() == null) {
			toDateTime = "99999999999999";
		} else {
			toDateTime = parm.getToDateTime();
		}
		EntityManager em = (EntityManager)applicationContext.getBean(EventConfiguration.ENTITY_MANAGER_NAME);

		System.out.println("fromTimestamp(" + fromDateTime + ") toTimestamp(" + toDateTime + ")");
		
		List<EventCheckpointEntity> checkpointList = em
				.createNamedQuery(EventCheckpointEntity.FIND_BY_DATETIME_FIRST, EventCheckpointEntity.class)
				.setParameter("fromDateTime", fromDateTime).setParameter("toDateTime", toDateTime)
				.setMaxResults(QUERY_MAX_SIZE).getResultList();

		while (checkpointList != null && checkpointList.size() != 0) {
			for (EventCheckpointEntity checkpoint : checkpointList) {
				String eventMessage = mergeEventMessage(checkpoint, em);
				TranEvent tranEvent = StringUtil.stringToTranEventEventObject(eventMessage);
				kafkaUtil.resendMqMessage(tranEvent.getTopicMessages(), tranEvent.getMessageIdPrefix(), false);
				kafkaEventProducer.sendEventMessage(tranEvent);
				em.detach(checkpoint);
				logger.info(MessageFormat.get(EventMessageId.EVENT_RECOVERY_EXECUTE), tranEvent.toString());
			}

			if (checkpointList.size() >= QUERY_MAX_SIZE) {
				EventCheckpointEntity lastEntity = checkpointList.get(QUERY_MAX_SIZE - 1);
				fromDateTime = lastEntity.getTranEventKey().getDatetime();

				System.out.println("fromTimestamp(" + fromDateTime + ") toTimestamp(" + toDateTime + ")");
				checkpointList = em
						.createNamedQuery(EventCheckpointEntity.FIND_BY_DATETIME_NEXT, EventCheckpointEntity.class)
						.setParameter("fromDateTime", fromDateTime)
						.setParameter("toDateTime", toDateTime)
						.setParameter("tranid", lastEntity.getTranEventKey().getTranId())
						.setMaxResults(QUERY_MAX_SIZE).getResultList();
			} else {
				checkpointList = null;
			}
		}
	}
	
	/**
	 * @param checkpoint
	 * @param em
	 * @return
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
				throw new EventCommandException("aaaaaaaaaaa");
			}
			stringBuilder.append(entity.getEventMsg());
			em.detach(checkpoint);
		}
		return stringBuilder.toString();
	}
}
