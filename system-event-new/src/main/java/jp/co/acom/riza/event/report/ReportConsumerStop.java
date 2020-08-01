package jp.co.acom.riza.event.report;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.config.EventMessageId;
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
					logger.error(MessageFormat.getMessage(EventMessageId.EVENT_EXCEPTION),e);
				}
			}
		};
		stop.start();
	}
}
