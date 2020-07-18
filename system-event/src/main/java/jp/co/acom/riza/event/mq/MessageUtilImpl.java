package jp.co.acom.riza.event.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsBinding;
import org.apache.camel.component.jms.JmsMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.mq.MQC;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.headers.internal.Header;
import com.ibm.mq.jmqi.MQPMO;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSession;
import com.ibm.msg.client.jms.JmsConstants;
import com.ibm.msg.client.jms.JmsDestination;
import com.ibm.msg.client.jms.JmsMessage;
import com.ibm.msg.client.wmq.WMQConstants;

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
	JmsOperations jmsOperations;
	 
	@Autowired
	Environment env;

	@Autowired
	MQQueueConnectionFactory mqQueueConnectionFactory;
	
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

	public int getMessageCount() {
		return messageMap.keySet().size();
	}
	/**
	 * キャッシュされたメツセージを全て取り出し送信する。
	 * @throws NamingException
	 * @throws JMSException 
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void flush() throws NamingException {
		if (env.getProperty(MQConstants.MQ_MOCK,Boolean.class,false)) {
			return;
		}
				
		if (!messageMap.isEmpty()) {

			ProducerTemplate producer = camelContext.createProducerTemplate();

			for (Entry<String, ArrayList<String>> entry : messageMap.entrySet()) {
				ArrayList<String> list = entry.getValue();
				String queName = entry.getKey();
				
				for (String putMessage : list) {
					Map<String , Object> map = new HashMap<String, Object>();
					map.put(WMQConstants.JMS_IBM_MQMD_MSGID,"123456789012345678901234".getBytes());
////					map.put(JmsConstants.), remappingFunction)
//					map.put(JmsConstants.JMS_IBM_MQMD_MSGID, "123456789012345678901234".getBytes());
//
//					map.put(JmsConstants.JMS_IBM_REPORT_PASS_MSG_ID, MQC.MQRO_PASS_MSG_ID);
//					map.put(JmsConstants.JMS_IBM_MSGTYPE, MQC.MQMT_REPORT);
//					map.put(JmsConstants.JMS_IBM_MSGTYPE, MQC.MQMT_REPORT);
					producer.sendBodyAndHeaders("jms:queue:" + queName, putMessage, map);
//						producer.sendBodyAndHeader(body, header, headerValue);("jms:queue:" + queName
//				 	, putMessage,JmsConstants.JMS_IBM_MQMD_MSGID,"123456789012345678901234".getBytes(),
//							JmsConstants.JMS_IBM_MQMD_CORRELID,"aaaaaaaaaaaaa");
//					, putMessage,JmsConstants.JMS_IBM_MQMD_MSGID,"123456789012345678901234".getBytes());
//					, putMessage,JmsHeaders.MESSAGE_ID,"id:1234567890");
					
					//jmsOperations.convertAndSend(queName,putMessage);
//					TextMessage msg = new TextMessage
					jmsOperations.send(queName,messageCreator -> {
						TextMessage message = messageCreator.createTextMessage(putMessage);
						message.setObjectProperty(WMQConstants.JMS_IBM_MQMD_MSGID, "123456789012345678901234".getBytes());
						return message;
					});
					
//					MQQueueConnection con = (MQQueueConnection) mqQueueConnectionFactory.createQueueConnection();
//					MQQueueSession session = (MQQueueSession) con.createQueueSession(false, WMQConstants.WMQ_MDCTX_SET_IDENTITY_CONTEXT);
					
					logger.debug("producer.send()" + putMessage);
				}
			}
		}
		messageMap.clear();
	}
}
