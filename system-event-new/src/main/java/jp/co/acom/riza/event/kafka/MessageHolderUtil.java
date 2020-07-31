package jp.co.acom.riza.event.kafka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.persist.PostCommitPersistentNotifier;
import jp.co.acom.riza.event.utils.ModeUtil;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * メッセージホルダーのユーティティ
 * @author teratani
 *
 */
@Component
@Scope(value = "transaction", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MessageHolderUtil {

	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(MessageHolderUtil.class);

	@Autowired
	CamelContext camelContext;

	@Autowired
	Environment env;

	@Autowired
	KafkaEventProducer kafkaProducer;
	
	@Autowired
	PostCommitPersistentNotifier postNotifier;

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
		postNotifier.setPersistentEvent(true);
	}

	/**
	 * キャッシュされたメツセージを全て取り出し送信する。
	 * 
	 * @throws NamingException
	 * @throws IOException
	 * @throws JMSException
	 */
	public void flush(byte[] messagIdPrefix) throws NamingException, IOException {
		
		if (ModeUtil.isKafkaMock()) {
			messageMap.clear();
			return;
		}

		String key = messagIdPrefix.toString();
		if (!messageMap.isEmpty()) {
			for (Entry<String, ArrayList<String>> entry : messageMap.entrySet()) {
				ArrayList<String> list = entry.getValue();
				String queName = entry.getKey();

				for (int i = 0; i < list.size(); i++) {
					String putMessage = list.get(i);
					kafkaProducer.sendTopicMqMessage(queName, key, putMessage,
							MessageUtil.createMessageId(messagIdPrefix, i));
					logger.debug("producer.send()" + putMessage);
				}
			}
		}
		messageMap.clear();
	}

	/**
	 * メッセージホルダーオブジェクトを取得
	 * @return
	 */
	public HashMap<String, ArrayList<String>> getMessageMap() {
		return messageMap;
	}
}
