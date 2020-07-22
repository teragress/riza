package jp.co.acom.riza.event.kafka;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaManualCommit;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.system.utils.log.Logger;

/**
 * マニュアルコミット
 *
 * @author developer
 *
 */
@Component(ManualCommitProcess.PROCESS_ID)
public class ManualCommitProcess implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(ManualCommitProcess.class);
	
	public static final String PROCESS_ID = "manualCommitProcess";
	/**
	 * マニュアルコミット
	 */
	public void process(Exchange exchange) throws Exception {
		logger.debug("ManualCommit#process started.");
		KafkaManualCommit manual = exchange.getIn().getHeader(KafkaConstants.MANUAL_COMMIT, KafkaManualCommit.class);
		manual.commitSync();
	}
}
