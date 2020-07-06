package jp.co.acom.riza.event.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	/**
	 *
	 */
	private HashMap<String, ArrayList<String>> messageMap = new HashMap<>();

	/**
	 * ストリングメッセージをキャッシュする。
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

	/**
	 * キャッシュされたメツセージを全て取り出し送信する。
	 * @throws NamingException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void flush() throws NamingException {
		if (!messageMap.isEmpty()) {

			ProducerTemplate producer = camelContext.createProducerTemplate();

			for (Entry<String, ArrayList<String>> entry : messageMap.entrySet()) {
				ArrayList<String> list = entry.getValue();
				String queName = entry.getKey();
				for (String putMessage : list) {
					producer.requestBody("mqm:queue" + queName, putMessage);
					logger.debug("producer.send()" + putMessage);
				}
			}
		}
		messageMap.clear();
	}
}
