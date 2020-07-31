package jp.co.acom.riza.event.kafka;

import org.apache.camel.ProducerTemplate;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import jp.co.acom.riza.event.msg.Manager;
import jp.co.acom.riza.event.msg.DomainEvent;
import jp.co.acom.riza.event.msg.Entity;
import jp.co.acom.riza.event.msg.TranEvent;
import jp.co.acom.riza.event.msg.EntityEvent;
import jp.co.acom.riza.event.utils.ModeUtil;
import jp.co.acom.riza.event.utils.StringUtil;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * Kafkaイベント用Producer処理
 *
 * @author teratani
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
		if (ModeUtil.isKafkaMock()) {
			return;
		}

		for (Manager pManager : tranEvent.getManagers()) {
			DomainEvent domainEvent = new DomainEvent();
			domainEvent.setHeader(tranEvent.getHeader());
			domainEvent.setManager(pManager.getManager());
			domainEvent.setRevision(pManager.getRevison());
			domainEvent.setEntitys(pManager.getEntitys());
			String domainTopic = KafkaConstants.KAFKA_DOMAIN_TOPIC_PREFIX + pManager.getManager()
					+ tranEvent.getHeader().getBusinessProcess();
			String domainMessage = StringUtil.objectToJsonString(domainEvent);
			logger.debug("kafka-domain-send topic=" + domainEvent + " message=" + domainMessage);
			send(domainTopic, domainMessage);

			for (Entity pEntity : pManager.getEntitys()) {
				EntityEvent pEvent = new EntityEvent();
				pEvent.setHeader(tranEvent.getHeader());
				pEvent.setManager(pManager.getManager());
				pEvent.setDomain(pManager.getDomain());
				pEvent.setRevision(pManager.getRevison());
				pEvent.setEntity(pEntity);
				String entityTopic = KafkaConstants.KAFKA_ENTITY_TOPIC_PREFIX
						+ pEntity.getEntity().substring((pEntity.getEntity().lastIndexOf('.') + 1));
				String entityMessage = StringUtil.objectToJsonString(pEvent);
				logger.debug("kafka-entity-send topic=" + entityTopic + " message=" + entityMessage);

				send(entityTopic, entityMessage);
			}
		}
	}

	/**
	 * KAFKAメッセージを送信して、送信したメッセージメタ情報を返す
	 * 
	 * @param topic   トピック名
	 * @param message 送信メッセージ
	 * @return 送信メッセージのメタ情報
	 */
	private ListenableFuture<SendResult<String, String>> send(String topic, String message) {
		return kafkaTemplate.send(topic, message);
	}

	/**
	 * MQ用のKAFKAメッセージを送信する<br>
	 * MQ用はKAFKAヘッダーにメッセージIDとキーを付与する。キーは同一トランザクションで同じパーティションにアサインする目的。
	 * 
	 * @param topic       トピック名
	 * @param key         キー
	 * @param message     送信メッセージ
	 * @param mqMessageID メッセージID
	 * @return 送信メッセージのメタ情報
	 */
	public ListenableFuture<SendResult<String, String>> sendTopicMqMessage(String topic, String key, String message,
			byte[] mqMessageID) {
		ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topic, message);
		rec.headers().add(KafkaConstants.KAFKA_HEADER_MQ_MESSAGE_ID, mqMessageID);

		return kafkaTemplate.send(rec);
	}
}
