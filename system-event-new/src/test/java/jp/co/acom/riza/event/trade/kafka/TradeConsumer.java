package jp.co.acom.riza.event.trade.kafka;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import jp.co.acom.riza.event.utils.ModeUtil;
import jp.co.acom.riza.system.utils.log.Logger;

//@Component
public class TradeConsumer extends RouteBuilder {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(TradeConsumer.class);

	@Autowired
	Environment env;

	/**
	 * カスタマービジネスプロセス
	 */
	@Override
	public void configure() throws Exception {
		logger.debug("configure() start");
		from("direct:" + "ENTITYAD_TRADE_EntityTrade_TradeBusiness")
		.routeId("ENTITYAD_TRADE_EntityTrade_TradeBusiness")
		.autoStartup(ModeUtil.isRouteStart())		
//    	.process(EntityConsumerInitilizer.PROCESS_ID)		
		.process("customerBusinessProcess");
	}
}
