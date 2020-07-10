package jp.co.acom.riza.event.kafka;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.msg.Manager;
import jp.co.acom.riza.event.msg.Entity;
import jp.co.acom.riza.event.msg.TranEvent;
import jp.co.acom.riza.event.msg.EntityEvent;
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * Kafkaイベント用Producer処理
 *
 * @author developer
 *
 */
@Service
public class KafkaEventProducer {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(KafkaEventProducer.class);

	@Autowired
	ProducerTemplate producer;
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	/**
	 * パーシステントイベントのKafka送信
	 * 
	 * @param tranEvent
	 */
	public void sendEventMessage(TranEvent tranEvent) {
		logger.debug("sendMessageEvent() started.");

		for (Manager pManager : tranEvent.getManagers()) {
			for (Entity pEntity : pManager.getEntitys()) {
				EntityEvent pEvent = new EntityEvent();
				pEvent.setHeader(tranEvent.getHeader());
				pEvent.setManager(pManager.getManager());
				pEvent.setRevision(pManager.getRevison());
				pEvent.setEntity(pEntity);
				String entityTopic = KafkaConstants.KAFKA_ENTITY_TOPIC_PREFIX
						+ pEntity.getEntity().substring((pEntity.getEntity().lastIndexOf('.') + 1));
				String entityMessage = StringUtil.objectToJsonString(pEvent);
				logger.debug("kafka-entity-send topic=" + entityTopic + " message=" + entityMessage);
				kafkaTemplate.send(entityTopic, entityMessage);
			}
		}
	}
}
