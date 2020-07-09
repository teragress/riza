package jp.co.acom.riza.event.kafka;

import java.io.IOException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jp.co.acom.riza.system.utils.log.Logger;

/**
 * ビジネス動的呼出しプロセス
 *
 * @author developer
 *
 */
@Service
public class TransactionRouteExecute {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(TransactionRouteExecute.class);
	
	/**
	 * Producer Template
	 */
	@Autowired
	ProducerTemplate template;
	
	/**
	 * @param route
	 * @param exchange
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void executeRoute(String route, Exchange exchange) {
		logger.debug("executeRoute");
		template.requestBodyAndHeaders(route, exchange.getIn().getBody(), exchange.getIn().getHeaders());
	}
}
