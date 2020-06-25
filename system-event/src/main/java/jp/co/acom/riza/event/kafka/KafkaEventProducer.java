package jp.co.acom.riza.event.kafka;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.msg.Manager;
import jp.co.acom.riza.event.msg.Entity;
import jp.co.acom.riza.event.msg.FlowEvent;
import jp.co.acom.riza.event.msg.EntityEvent;
import jp.co.acom.riza.event.utils.StringUtil;
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
				StringUtil.objectToJsonString(flowEvent));

		for (Manager pManager : flowEvent.getManagers()) {
			for (Entity pEntity : pManager.getEntitys()) {
				EntityEvent pEvent = new EntityEvent();
				pEvent.setHeader(flowEvent.getHeader());
				pEvent.setManager(pManager.getManager());
				pEvent.setEntity(pEntity);
				String cls = pEntity.getEntity().substring((pEntity.getEntity().lastIndexOf('.') + 1));
				kafkaTemplate.send(KafkaConstants.KAFKA_ENTITY_TOPIC_PREFIX + cls,
						StringUtil.objectToJsonString(pEvent));
			}
		}
	}
}
