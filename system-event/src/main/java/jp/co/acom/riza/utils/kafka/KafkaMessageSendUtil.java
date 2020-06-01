package jp.co.acom.riza.utils.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jp.co.acom.riza.utils.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.naming.NamingException;

//import java.util.Properties;
import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * @author developer
 *
 */
@Configurable
public class KafkaMessageSendUtil {

	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(KafkaMessageSendUtil.class);
	/**
	 * 
	 */
	@Autowired
	CamelContext camelContext;

	/**
	 * 
	 */
	private HashMap<String, ArrayList<String>> messageMap = new HashMap<>();

	/**
	 * KAFKAメッセージ送信ユーティリティ
	 */
	public KafkaMessageSendUtil() {
		super();
	}

	/**
	 * ストリングメッセージをキャッシュする。
	 * 
	 * @param queName キュー名
	 * @param message ストリングメッセージ
	 */
	public void pushString(String queName, String message) {
		logger.debug("pushString() queName(" + queName + ") message" + message);
		String topicName = jp.co.acom.riza.utils.kafka.KafkaUtilConstants.KAFKA_TOPIC_PREFIX + queName;

		ArrayList<String> topicMessage = messageMap.get(topicName);
		if (topicMessage == null) {
			logger.debug("pushiString() create ArrayList");
			topicMessage = new ArrayList<String>();
			messageMap.put(topicName, topicMessage);
		}
		topicMessage.add(message);
	}

	/**
	 * オブジェクトをJSON形式に変換してキャッシュする。
	 * 
	 * @param topicName トピック名
	 * @param object    オブジェクト
	 * @throws JsonProcessingException JSON処理例外
	 */
	public void pushJsonObject(String topicName, Object object) throws JsonProcessingException {

		ArrayList<String> topicMessage = messageMap.get(topicName);

		if (topicMessage == null) {
			topicMessage = new ArrayList<String>();
			messageMap.put(topicName, topicMessage);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			String jsonMessage = mapper.writeValueAsString(object);

			topicMessage.add(jsonMessage);
		} catch (JsonProcessingException ex) {
			throw ex;
		}
	}

	/**
	 * キャッシュされたメツセージを全て取り出し送信する。
	 * @throws NamingException 
	 */
	public void flush() throws NamingException {
		if (!messageMap.isEmpty()) {
			
			ProducerTemplate producer = camelContext.createProducerTemplate();
			
			for (Entry<String, ArrayList<String>> entry : messageMap.entrySet()) {
				ArrayList<String> list = entry.getValue();
				String topicName = entry.getKey();
				for (String putMessage : list) {
					String uuidStr = UUID.randomUUID().toString();
					producer.requestBodyAndHeader("direct:" + topicName, putMessage, KafkaConstants.KEY,
							uuidStr, String.class);
					logger.debug("producer.send()" + putMessage);
				}
			}
		}
		messageMap.clear();
	}
}
