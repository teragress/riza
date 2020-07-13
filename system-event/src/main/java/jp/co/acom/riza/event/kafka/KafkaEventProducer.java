package jp.co.acom.riza.event.kafka;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.camel.ProducerTemplate;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import jp.co.acom.riza.event.msg.Manager;
import jp.co.acom.riza.event.command.parm.ExecTableCreanParm;
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
	@Autowired
	Environment env;

	Boolean mock;
	
	@PostConstruct
	public void initilize() {
		mock = env.getProperty(KafkaConstants.KAFKA_MOCK,Boolean.class,false);
	}
	/**
	 * パーシステントイベントのKafka送信
	 * 
	 * @param tranEvent
	 */
	public void sendEventMessage(TranEvent tranEvent) {
		if (mock) {
			return;
		}
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

				// kafkaTemplate.send(entityTopic, entityMessage);
//				ListenableFuture<SendResult<String, String>> sendResultList =  kafkaTemplate.send(entityTopic, entityMessage);
				ListenableFuture<SendResult<String, String>> sendResultList = kafkaTemplate.send(entityTopic,
						entityMessage);
				// .get(10L,TimeUnit.SECONDS);
				try {
					System.out.println(
							"************************** kafka producer result ************************************");
					System.out.println("********partation=" + sendResultList.get().getRecordMetadata().partition());
					System.out.println("********offset=" + sendResultList.get().getRecordMetadata().offset());
				} catch (InterruptedException | ExecutionException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
		
			}
		}
	}
}
