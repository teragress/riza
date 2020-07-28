package jp.co.acom.riza.event.kafka;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.acom.riza.system.utils.log.Logger;

/**
 * トランザクション生成のビジネスルート呼出し
 *
 * @author teratani
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
	 * トランザクション生成のビジネスルート呼出し
	 * @param route 実行ルートID
	 * @param exchange camel exchange
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void executeRoute(String route, Exchange exchange) {
		logger.debug("executeRoute");
		template.requestBodyAndHeaders(route, exchange.getIn().getBody(), exchange.getIn().getHeaders());
	}
}
