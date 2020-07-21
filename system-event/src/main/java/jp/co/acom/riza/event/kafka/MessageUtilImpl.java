package jp.co.acom.riza.event.kafka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import jp.co.acom.riza.event.msg.KafkaMessage;
import jp.co.acom.riza.event.msg.KafkaTopics;
import jp.co.acom.riza.system.utils.log.Logger;

@Component
@Scope(value = "transaction", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MessageUtilImpl {

	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(MessageUtilImpl.class);

	@Autowired
	CamelContext camelContext;

	@Autowired
	Environment env;

	@Autowired
	KafkaEventProducer kafkaProducer;

	private HashMap<String, ArrayList<String>> messageMap = new HashMap<>();

	/**
	 * ストリングメッセー ProducerTemplate producer = camelContext.createProducerTemplate();
	 * ジをキャッシュする。
	 *
	 * @param queName キュー名
	 * @param message ストリングメッセージ
	 */

	public void pushString(String queName, String message) {
		logger.debug("pushString() queName(" + queName + ") message" + message);

		ArrayList<String> topicMessage = messageMap.get(queName);
		if (topicMessage == null) {
			logger.debug("pushiString() create ArrayList");
			topicMessage = new ArrayList<String>();
			messageMap.put(queName, topicMessage);
		}
		topicMessage.add(message);
	}

	public int getMessageCount() {
		return messageMap.keySet().size();
	}

	/**
	 * キャッシュされたメツセージを全て取り出し送信する。
	 * 
	 * @throws NamingException
	 * @throws IOException
	 * @throws JMSException
	 */
	public void flush(byte[] messagIdPrefix) throws NamingException, IOException {
		if (env.getProperty(KafkaConstants.KAFKA_MOCK, Boolean.class, false)) {
			return;
		}

		if (!messageMap.isEmpty()) {
			for (Entry<String, ArrayList<String>> entry : messageMap.entrySet()) {
				ArrayList<String> list = entry.getValue();
				String queName = entry.getKey();

				for (int i = 0; i < list.size(); i++) {
					String putMessage = list.get(i);
					kafkaProducer.sendReportTopic(queName, putMessage, MessageUtil.createMessageId(messagIdPrefix, i));
					logger.debug("producer.send()" + putMessage);
				}
			}
		}
		messageMap.clear();
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<KafkaTopics> saveReportMessage(byte[] messagIdprefix)
			throws IOException, InterruptedException, ExecutionException {
		logger.debug("saveReportMessage() startes.");

		List<KafkaTopics> topicMessages = new ArrayList<KafkaTopics>();

		if (env.getProperty(KafkaConstants.KAFKA_MOCK, Boolean.class, false)) {
			return topicMessages;
		}

		byte[] messagePrefix = MessageUtil.getUniqueID();

		for (Entry<String, ArrayList<String>> entry : messageMap.entrySet()) {
			String queName = entry.getKey();
			KafkaTopics topicMessage = new KafkaTopics();
			topicMessage.setTopic(queName);
			topicMessages.add(topicMessage);

			ArrayList<String> list = entry.getValue();

			for (int i = 0; i < list.size(); i++) {
				String putMessage = list.get(i);

				byte[] messageId = MessageUtil.createMessageId(messagePrefix, i);
				ListenableFuture<SendResult<String, String>> result = kafkaProducer
						.sendReportTopic(KafkaConstants.KAFKA_SAVE_TOPIC_PREFIX + queName, putMessage, messageId);
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
