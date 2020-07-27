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
 * @author teratani
 *
 */
@Service(ReportConsumerStop.PROCESS_ID)
public class ReportConsumerStop implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(ReportConsumerStop.class);

	public static final String PROCESS_ID = "reportConsumerStop";

	/**
	 * レポートコンシューマ停止
	 * 
	 * @throws Exception
	 */
	public void process(Exchange exchange) throws Exception {
		logger.debug("process() started.");

		Thread stop = new Thread() {
			@Override
			public void run() {
				try {
					exchange.getContext().stopRoute(ReportConsumer.REPORT_CONSUMER_ROUTE_ID);
					
				} catch (Exception e) {
					logger.error(MessageFormat.get(EventMessageId.EVENT_EXCEPTION),e);
				}
			}
		};
		stop.start();
	}
}
