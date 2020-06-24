package jp.co.acom.riza.event.kafka;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.msg.EntityManagerPersistent;
import jp.co.acom.riza.event.msg.EntityPersistent;
import jp.co.acom.riza.event.msg.FlowEvent;
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
		kafkaTemplate.send(KafkaConstants.KAFKA_FLOW_TOPIC_PREFIX + flowEvent.getFlowId(), JsonConverter.objectToJsonString(flowEvent));
//		producer.sendBodyAndHeader("direct:kafkaProducer", JsonConverter.objectToJsonString(flowEvent),
//				"topicName", KafkaConstants.KAFKA_FLOW_TOPIC_PREFIX + flowEvent.getFlowId());

		for (EntityManagerPersistent eM : flowEvent.getEntityManagerPersistences()) {
			for (EntityPersistent eP : eM.getEntityPersistences()) {
				String[] sStr = eP.getEntityClassName().split(".");
				String topic = sStr[sStr.length - 1];
				kafkaTemplate.send(KafkaConstants.KAFKA_ENTITY_TOPIC_PREFIX + topic,
						JsonConverter.objectToJsonString(eP));
//				producer.sendBodyAndHeader("direct:kafkaProducer", JsonConverter.objectToJsonString(eP),
//						"topicName", KafkaConstants.KAFKA_FLOW_TOPIC_PREFIX + topic);
			}
		}
	}
}
