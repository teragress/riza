package jp.co.acom.riza.event.flow;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import jp.co.acom.riza.utils.log.Logger;

@Component
public class CustomerFlowConsumer extends RouteBuilder {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(CustomerFlowConsumer.class);

	@Autowired
	Environment env;

	/**
	 * カスタマービジネスプロセス
	 */
	@Override
	public void configure() throws Exception {

		logger.debug("configure() start");
		from("direct:" + "KAD_InsertGroup_FlowInsertApl_InsertBusiness")
		.routeId("KAD_InsertGroup_FlowInsertApl_InsertBusiness")
		.process("customerBusinessProcess");
	}
}
