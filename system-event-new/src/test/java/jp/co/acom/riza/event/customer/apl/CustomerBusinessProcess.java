package jp.co.acom.riza.event.customer.apl;

import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.system.utils.log.Logger;

/**
 * サンプル Processor.
 *
 * <p>
 * メッセージの body 部をログに出力する.
 */
@Service
public class CustomerBusinessProcess implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(CustomerBusinessProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("process() started.");
		Map<String, Object> headers = exchange.getIn().getHeaders();
		logger.info("body=" + exchange.getIn().getBody());
		for (String key : headers.keySet()) {
			logger.info("key=" + key + " value=" + headers.get(key));
		}
	}
}