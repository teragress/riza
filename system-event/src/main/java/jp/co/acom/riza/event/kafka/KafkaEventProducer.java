package jp.co.acom.riza.event.kafka;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.msg.Manager;
import jp.co.acom.riza.event.msg.Entity;
import jp.co.acom.riza.event.msg.FlowEvent;
import jp.co.acom.riza.event.msg.EntityEvent;
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

		for (Manager pManager : flowEvent.getManagers()) {
			for (Entity pEntity : pManager.getEntitys()) {
				EntityEvent pEvent = new EntityEvent();
				pEvent.setHeader(flowEvent.getHeader());
				pEvent.setManager(pManager.getManager());
				pEvent.setEntity(pEntity);
				String[] sStr = pEntity.getEntity().split(".");
				String topic = sStr[sStr.length - 1];
				kafkaTemplate.send(KafkaConstants.KAFKA_ENTITY_TOPIC_PREFIX + topic,
						JsonConverter.objectToJsonString(pEvent));
			}
		}
	}
}
