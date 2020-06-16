package jp.co.acom.riza.event.kafka;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaManualCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.utils.log.Logger;

/**
 * マニュアルコミット
 *
 * @author developer
 *
 */
@Component
public class DynamicExecuteProcess implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(DynamicExecuteProcess.class);
	/**
	 * Producer Template
	 */
	@Autowired
	ProducerTemplate template;

	@Autowired
	ApplicationRouteHolder holder;

	/**
	 * マニュアルコミット
	 */
	public void process(Exchange exchange) throws Exception {
		logger.debug("process() started.");
		String[] ids = exchange.getFromRouteId().split("_");
		String group = ids[1];
		String topic = exchange.getIn().getHeader(KafkaConstants.TOPIC,String.class);
		for (String root : holder.getApplicationRoutes(group, topic)) {
			template.asyncRequestBody("direct:" + root, exchange.getIn().getBody());
		}


		KafkaManualCommit manual = exchange.getIn().getHeader(KafkaConstants.MANUAL_COMMIT, KafkaManualCommit.class);
		manual.commitSync();
	}
}
