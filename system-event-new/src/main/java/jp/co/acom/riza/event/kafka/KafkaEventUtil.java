package jp.co.acom.riza.event.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

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

import jp.co.acom.riza.event.cmd.parm.KafkaMessageInfo;
import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.event.exception.EventCommandException;
import jp.co.acom.riza.event.msg.KafkaMessage;
import jp.co.acom.riza.event.msg.KafkaTopicMessage;
import jp.co.acom.riza.event.utils.ModeUtil;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

/**
 * KAFKAイベント用ユーティリティ
 * 
 * @author teratani
 *
 */
@Service
public class KafkaEventUtil {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(KafkaEventUtil.class);

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
	 * 初期化処理としてKAFKA接続用のプロパティを設定する
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
	 * プロパティの設定メソッド(例外となるため未指定のものは設定しない)
	 * 
	 * @param key キー
	 * @param value 値
	 */
	private void putPropertie(String key, Object value) {
		if (value != null) {
			props.put(key, value);
		}
	}

	/**
	 * パーシステントイベントのKafka再送信
	 * 
	 * @param msgInfoList 送信用メッセージ情報リスト
	 * @return 再送信したメッセージ情報リスト
	 * @throws Exception
	 */
	public ArrayList<KafkaMessageInfo> recoveryKafkaMessages(List<KafkaMessageInfo> msgInfoList) throws Exception {
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
	 * メタ情報(パーティション、オフセットなど)指定のKAFKAメッセージ取得
	 * 
	 * @param topic トピック
	 * @param partition パーティション
	 * @param offset オフセット
	 * @return メッセージのレコード
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
				logger.error(MessageFormat.getMessage(EventMessageId.KAFKA_MESSAGE_NOT_FOUND, topic, partition, offset));
				throw new EventCommandException("kafka message notfound topic(" + topic + ") partition(" + partition
						+ ") offset(" + offset + ")");
			}
		}
		return conrec;
	}

	/**
	 * 
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
	 * MQ用KAFKAメッセージの再送信
	 * 
	 * @param topics トピック
	 * @param messageIdprefix メッセージIDプレフィックス
	 * @param notFoundError 対象メッセージが存在しない場合にエラーとするか
	 * @return 
	 * @throws Exception
	 */
	public List<KafkaTopicMessage> resendMqMessage(List<KafkaTopicMessage> topics, byte[] messageIdprefix,
			boolean notFoundError) throws Exception {
		logger.debug("saveMqMessage() started.");

		List<KafkaTopicMessage> topicMessages = new ArrayList<KafkaTopicMessage>();

		for (KafkaTopicMessage topicMessage : topics) {
			int i = 0;
			for (KafkaMessage kafkaMessage : topicMessage.getKmsg()) {

				ConsumerRecord<String, String> conrec = getKafkaMessage(topicMessage.getTopic(),
						kafkaMessage.getPartition(), kafkaMessage.getOffset());
				if (conrec == null && notFoundError) {
					logger.error(MessageFormat.getMessage(EventMessageId.SAVE_KAFKA_MESSAGE_NOT_FOUND,
							topicMessage.getTopic(), kafkaMessage.getPartition(), kafkaMessage.getOffset()));
					throw new EventCommandException("Save kafka message not found");
				}
				if (conrec != null) {
					kafkaEventProducer.sendTopicMqMessage(topicMessage.getTopic(), messageIdprefix.toString(),
							conrec.value(), MessageUtil.createMessageId(messageIdprefix, i));
					logger.debug("producer.send()" + conrec.value());
				}
				i++;
			}
		}
		return topicMessages;
	}

	/**
	 * MQメッセージ用のKAFKAメッセージをコミット前に退避するメソッド
	 * 
	 * @param messageIdprefix メッセージIDプレフィックス
	 * @return
	 * @throws Exception
	 */
	public List<KafkaTopicMessage> saveMqMessage(byte[] messagIdprefix)
			throws Exception {
		logger.debug("saveMqMessage() started.");

		List<KafkaTopicMessage> topicMessages = new ArrayList<KafkaTopicMessage>();

		if (ModeUtil.isKafkaMock()) {
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
