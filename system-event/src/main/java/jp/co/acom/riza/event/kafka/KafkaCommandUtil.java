package jp.co.acom.riza.event.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import jp.co.acom.riza.event.command.parm.KafkaMessageInfo;
import jp.co.acom.riza.exception.EventCommandException;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

/**
 * Kafkaイベント用Producer処理
 *
 * @author developer
 *
 */
@Service
public class KafkaCommandUtil {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(KafkaCommandUtil.class);

	@Autowired
	Environment env;

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	private Properties props = new Properties();

	@PostConstruct
	public void initProperties() {

		putPropertie(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty(KafkaConstants.KAFKA_BOOTSTRAP_SERVER));
		putPropertie(SslConfigs.DEFAULT_SSL_PROTOCOL, env.getProperty(KafkaConstants.KAFKA_SECURITY_PROTOCOL));
		putPropertie(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, env.getProperty(KafkaConstants.KAFKA_KEYSTORE_TYPE));
		putPropertie(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, env.getProperty(KafkaConstants.KAFKA_KEYSTORE_LOCATION));
		putPropertie(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, env.getProperty(KafkaConstants.KAFKA_KEYSTORE_PASSWORD));
		putPropertie(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, env.getProperty(KafkaConstants.KAFKA_TRUSTSTORE_TYPE));
		putPropertie(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
				env.getProperty(KafkaConstants.KAFKA_TRUSTSTORE_LOCATION));
		putPropertie(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
				env.getProperty(KafkaConstants.KAFKA_TRUSTSTORE_PASSWORD));

		putPropertie(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		putPropertie(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		putPropertie(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.KAFKA_RECOVERY_COMMAND_CONSUMER_GROUP);
	}

	private void putPropertie(String key, Object value) {
		if (value != null) {
			props.put(key, value);
		}
	}

	/**
	 * パーシステントイベントのKafka送信
	 * 
	 * @param tranEvent
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public ArrayList<KafkaMessageInfo> recoveryKafkaMessages(List<KafkaMessageInfo> msgInfoList)
			throws InterruptedException, ExecutionException {
		logger.debug("sendMessageEvent() started.");

		ArrayList<KafkaMessageInfo> respList = new ArrayList<KafkaMessageInfo>();
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

			for (KafkaMessageInfo msgInfo : msgInfoList) {
				TopicPartition topicPartition = new TopicPartition(msgInfo.getTopic(),
						msgInfo.getPartition().intValue());
				Collection<TopicPartition> col = new ArrayList<TopicPartition>();
				col.add(topicPartition);
				consumer.assign(col);
				consumer.seek(topicPartition, msgInfo.getOffset());
				ConsumerRecords<String, String> conRecs = consumer.poll(Duration.ofMillis(1000));
				ConsumerRecord<String, String> conrec = conRecs.iterator().next();
				if (conrec == null) {
					logger.error(MessageFormat.get("REV0001E"), msgInfo.getTopic(), msgInfo.getPartition(),
							msgInfo.getOffset());
					throw new EventCommandException(msgInfo.toString());
				}
				ListenableFuture<SendResult<String, String>> sendResultList = kafkaTemplate.send(msgInfo.getTopic(),
						conrec.value());
				KafkaMessageInfo rtnInf = new KafkaMessageInfo();
				rtnInf.setPartition(sendResultList.get().getRecordMetadata().partition());
				rtnInf.setOffset(sendResultList.get().getRecordMetadata().offset());
				rtnInf.setTopic(msgInfo.getTopic());
			}
		}
		return respList;
	}

	/**
	 * 
	 * @param tranEvent
	 */
	public ConsumerRecord<String, String> getKafkaMessage(KafkaMessageInfo msgInfo) {
		logger.debug("getKafkaMessage() started.");
		ConsumerRecord<String, String> conrec;
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

			TopicPartition topicPartition = new TopicPartition(msgInfo.getTopic(), msgInfo.getPartition());
			consumer.seek(topicPartition, msgInfo.getOffset());
			ConsumerRecords<String, String> conRecs = consumer.poll(Duration.ofMillis(1000));
			conrec = conRecs.iterator().next();
			if (conrec == null) {
				logger.error(MessageFormat.get("REV0001E"), msgInfo.getTopic(), msgInfo.getPartition(),
						msgInfo.getOffset());
				throw new EventCommandException(msgInfo.toString());
			}
		}
		return conrec;
	}

	/**
	 * 
	 * @param tranEvent
	 */
	public KafkaMessageInfo getKafkaMessageInfo(ConsumerRecord<String, String> conrec) {
		logger.debug("getKafkaMessage() started.");
		KafkaMessageInfo msgInfo = new KafkaMessageInfo();
		msgInfo.setPartition(conrec.partition());
		msgInfo.setOffset(conrec.offset());
		msgInfo.setTopic(conrec.topic());
		return msgInfo;
	}
}
