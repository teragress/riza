package jp.co.acom.riza.event.kafka;

import javax.transaction.Transactional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.utils.log.Logger;

/**
 * ビジネス動的呼出しプロセス
 *
 * @author developer
 *
 */
@Service
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
	 * ダイナミック業務呼出し
	 */
	public void process(Exchange exchange) {
		logger.debug("process() started.");
		String[] ids = exchange.getFromRouteId().split("_");
		String group = ids[1];
		String topic = exchange.getIn().getHeader(KafkaConstants.TOPIC, String.class);
		for (String root : holder.getApplicationRoutes(group, topic)) {
			logger.info("dynamic execute root=" + root + " group=" + group + " topic=" + topic);
			try {
				executeProcess("direct:" + root, exchange);
			} catch (Exception ex) {

			}
		}

//		KafkaManualCommit manual = exchange.getIn().getHeader(KafkaConstants.MANUAL_COMMIT, KafkaManualCommit.class);
//		manual.commitSync();
	}

	/**
	 * @param route
	 * @param exchange
	 */
	@Transactional
	private void executeProcess(String route, Exchange exchange) {
		try {
			template.requestBodyAndHeaders(route, exchange.getIn().getBody(), exchange.getIn().getHeaders());
		} catch (Exception ex) {
			logger.error("Business Transaction error occurred.",ex);
			new RuntimeException(ex);
		}
	}
}
