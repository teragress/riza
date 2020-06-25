package jp.co.acom.riza.event.kafka;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.msg.PersistentManager;
import jp.co.acom.riza.event.msg.PersistentEntity;
import jp.co.acom.riza.event.msg.FlowEvent;
import jp.co.acom.riza.event.msg.PersistentEvent;
import jp.co.acom.riza.event.utils.JsonConverter;
import jp.co.acom.riza.utils.log.Logger;

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
	 * @param flowEvent
	 */
	public void sendEventMessage(FlowEvent flowEvent) {
		logger.debug("sendMessageEvent() started.");
		
		kafkaTemplate.send(KafkaConstants.KAFKA_FLOW_TOPIC_PREFIX + flowEvent.getFlowId(),
				JsonConverter.objectToJsonString(flowEvent));

		for (PersistentManager pManager : flowEvent.getEntityManagerPersistences()) {
			for (PersistentEntity pEntity : pManager.getEntityPersistences()) {
				PersistentEvent pEvent = new PersistentEvent();
				pEvent.setEventHeader(flowEvent.getEventHeader());
				pEvent.setEntityManagerName(pManager.getEntityManagerName());
				pEvent.setEntityPersistence(pEntity);
				String[] sStr = pEntity.getEntityClassName().split(".");
				String topic = sStr[sStr.length - 1];
				kafkaTemplate.send(KafkaConstants.KAFKA_ENTITY_TOPIC_PREFIX + topic,
						JsonConverter.objectToJsonString(pEvent));
			}
		}
	}
}
