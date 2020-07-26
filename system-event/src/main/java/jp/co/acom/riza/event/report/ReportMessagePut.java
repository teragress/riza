package jp.co.acom.riza.event.report;

import javax.jms.TextMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Service;

import com.ibm.msg.client.wmq.WMQConstants;

import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.event.mq.MQConstants;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

/**
 * レポートメッセージのMQPUT
 *
 * @author developer
 *
 */
@Service(ReportMessagePut.PROCESS_ID)
public class ReportMessagePut implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(ReportMessagePut.class);

	public static final String PROCESS_ID = "reportMessagePut";

	/**
	 * Producer Template
	 */
	@Autowired
	JmsOperations jmsOperations;

	@Autowired
	Environment env;

	/**
	 * ダイナミック業務呼出し
	 * 
	 * @throws Exception
	 */
	public void process(Exchange exchange) throws Exception {
		logger.debug("process() started.");
		if (env.getProperty(MQConstants.MQ_MOCK,Boolean.class,false)) {
			return;
		}
		
		int retryCount = env.getProperty(MQConstants.MQ_PUT_MAX_RETRY, Integer.class,
				MQConstants.MQ_DEFAULT_PUT_MAX_RETRY);
		int retryTime = env.getProperty(MQConstants.MQ_PUT_RETRY_TIME, Integer.class,
				MQConstants.MQ_DEFAULT_PUT_RETRY_TIME);

		String topic = exchange.getIn().getHeader(KafkaConstants.TOPIC, String.class);
		Exception throwException = null;
		for (int i = 0; i < retryCount; i++) {
			try {
				jmsOperations.send(topic, messageCreator -> {
					TextMessage message = messageCreator.createTextMessage((String) exchange.getIn().getBody());
					message.setObjectProperty(WMQConstants.JMS_IBM_MQMD_MSGID, exchange.getIn()
							.getHeader(jp.co.acom.riza.event.kafka.KafkaConstants.KAFKA_HEADER_MQ_MESSAGE_ID));
					return message;
				});
				return;

			} catch (Exception ex) {
				throwException = ex;
				logger.warn(MessageFormat.get(EventMessageId.MQPUT_EXCEPTION), i + 1, retryCount, retryTime,
						ex.getMessage());
				logger.info(MessageFormat.get(EventMessageId.WARNING_EXCEPTION_INFORMATION), ex);
				Thread.sleep(retryTime * 1000);
			}
		}
		
		logger.warn(MessageFormat.get(EventMessageId.MQPUT_RETRY_OVER), retryCount, retryCount, retryTime,
				throwException.getMessage());
		throw throwException;
	}
}
