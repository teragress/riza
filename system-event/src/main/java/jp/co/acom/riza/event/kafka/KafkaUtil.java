package jp.co.acom.riza.event.kafka;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.apache.camel.component.kafka.KafkaProducer;
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

import jp.co.acom.riza.event.cmd.parm.KafkaMessageInfo;
import jp.co.acom.riza.event.msg.KafkaMessage;
import jp.co.acom.riza.event.msg.KafkaTopicMessage;
import jp.co.acom.riza.exception.EventCommandException;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

/**
 * @author vagrant
 *
 */
@Service
public class KafkaUtil {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(KafkaUtil.class);

	@Autowired
	Environment env;

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	MessageHolderUtil messageHolderUtil;

	@Autowired
	KafkaEventProducer kafkaEventProducer;

	private Properties props = new Properties();

	/**
	 * 
	 */
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
		putPropertie(ConsumerConfig.GROUP_ID_CONFIG,
				KafkaConstants.KAFKA_RECOVERY_COMMAND_CONSUMER_GROUP + env.getProperty("HOSTNAME"));
	}

	/**
	 * @param key
	 * @param value
	 */
	private void putPropertie(String key, Object value) {
		if (value != null) {
			props.put(key, value);
		}
	}

	/**
	 * パーシステントイベントのKafka送信
	 * 
	 * @param msgInfoList
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public ArrayList<KafkaMessageInfo> recoveryKafkaMessages(List<KafkaMessageInfo> msgInfoList)
			throws InterruptedException, ExecutionException {
		logger.debug("sendMessageEvent() started.");

		ArrayList<KafkaMessageInfo> respList = new ArrayList<KafkaMessageInfo>();
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

			for (KafkaMessageInfo msgInfo : msgInfoList) {
				ConsumerRecord<String, String> conrec = getKafkaMessage(msgInfo.getTopic(), msgInfo.getPartition(),
						msgInfo.getOffset());
				ListenableFuture<SendResult<String, String>> sendResultList = kafkaTemplate.send(msgInfo.getTopic(),
						conrec.value());
				KafkaMessageInfo rtnInf = new KafkaMessageInfo();
				rtnInf.setPartition(sendResultList.get().getRecordMetadata().partition());
				rtnInf.setOffset(sendResultList.get().getRecordMetadata().offset());
				rtnInf.setTopic(msgInfo.getTopic());
				respList.add(rtnInf);
			}
		}
		return respList;
	}

	/**
	 * @param topic
	 * @param partition
	 * @param offset
	 * @return
	 */
	public synchronized ConsumerRecord<String, String> getKafkaMessage(String topic, int partition, long offset) {
		logger.debug("getKafkaMessage() started.");
		ConsumerRecord<String, String> conrec;
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

			TopicPartition topicPartition = new TopicPartition(topic, partition);
			Collection<TopicPartition> col = new ArrayList<TopicPartition>();
			col.add(topicPartition);
			consumer.assign(col);
			consumer.seek(topicPartition, offset);
			ConsumerRecords<String, String> conRecs = consumer.poll(Duration.ofMillis(1000));
			conrec = conRecs.iterator().next();
			if (conrec == null) {
				logger.error(MessageFormat.get("REV0001E"), topic, partition, offset);
				throw new EventCommandException("kafka message notfound topic(" + topic + ") partition(" + partition
						+ ") offset(" + offset + ")");
			}
		}
		return conrec;
	}

	/**
	 * @param conrec
	 * @return
	 */
	public KafkaMessageInfo getKafkaMessageInfo(ConsumerRecord<String, String> conrec) {
		logger.debug("getKafkaMessage() started.");
		KafkaMessageInfo msgInfo = new KafkaMessageInfo();
		msgInfo.setPartition(conrec.partition());
		msgInfo.setOffset(conrec.offset());
		msgInfo.setTopic(conrec.topic());
		return msgInfo;
	}

	/**
	 * @param topics
	 * @param messageIdprefix
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<KafkaTopicMessage> resendMqMessage(List<KafkaTopicMessage> topics, byte[] messageIdprefix)
			throws IOException, InterruptedException, ExecutionException {
		logger.debug("saveMqMessage() started.");

		List<KafkaTopicMessage> topicMessages = new ArrayList<KafkaTopicMessage>();
		if (env.getProperty(KafkaConstants.KAFKA_MOCK, Boolean.class, false)) {
			return topicMessages;
		}

		for (KafkaTopicMessage topicMessage : topics) {
			int i = 0;
			for (KafkaMessage kafkaMessage : topicMessage.getKmsg()) {

				ConsumerRecord<String, String> conrec = getKafkaMessage(topicMessage.getTopic(),
						kafkaMessage.getPartition(), kafkaMessage.getOffset());
				if (conrec == null) {
					throw new RuntimeException("");
				}
				kafkaEventProducer.sendTopicMqMessage(topicMessage.getTopic(), messageIdprefix.toString(),
						conrec.value(), MessageUtil.createMessageId(messageIdprefix, i));
				logger.debug("producer.send()" + conrec.value());
				i++;
			}

		}
		return topicMessages;
	}

	/**
	 * @param messagIdprefix
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<KafkaTopicMessage> saveMqMessage(byte[] messagIdprefix)
			throws IOException, InterruptedException, ExecutionException {
		logger.debug("saveMqMessage() started.");

		List<KafkaTopicMessage> topicMessages = new ArrayList<KafkaTopicMessage>();

		if (env.getProperty(KafkaConstants.KAFKA_MOCK, Boolean.class, false)) {
			return topicMessages;
		}

		byte[] messagePrefix = MessageUtil.getUniqueID();
		String key = messagePrefix.toString();

		for (Entry<String, ArrayList<String>> entry : messageHolderUtil.getMessageMap().entrySet()) {
			String queName = entry.getKey();
			KafkaTopicMessage topicMessage = new KafkaTopicMessage();
			topicMessage.setTopic(queName);
			topicMessages.add(topicMessage);

			ArrayList<String> list = entry.getValue();

			for (int i = 0; i < list.size(); i++) {
				String putMessage = list.get(i);

				byte[] messageId = MessageUtil.createMessageId(messagePrefix, i);
				ListenableFuture<SendResult<String, String>> result = kafkaEventProducer.sendTopicMqMessage(
						KafkaConstants.KAFKA_SAVE_TOPIC_PREFIX + queName, key, putMessage, messageId);
				KafkaMessage kafkaMessage = new KafkaMessage();
				kafkaMessage.setPartition(result.get().getRecordMetadata().partition());
				kafkaMessage.setOffset(result.get().getRecordMetadata().offset());
				topicMessage.getKmsg().add(kafkaMessage);

				logger.debug("producer.send()" + putMessage);
			}
		}

		return topicMessages;
	}
}
