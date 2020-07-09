package jp.co.acom.riza.event.customer.kafka;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.kafka.EntityConsumerInitilizer;
import jp.co.acom.riza.system.utils.log.Logger;

//@Component
public class CustomerConsumer extends RouteBuilder {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(CustomerConsumer.class);

	@Autowired
	Environment env;

	/**
	 * カスタマービジネスプロセス
	 */
	@Override
//	@Transactional(rollbackOn = Throwable.class)
	public void configure() throws Exception {

		logger.debug("configure() start");
		from("direct:" + "KAD_CUSTOMER_EntityCustomer_CustomerBusiness")
		.routeId("KAD_CUSTOMER_EntityCustomer_CustomerBusiness")
    	.process(EntityConsumerInitilizer.PROCESS_ID)		
		.process("customerBusinessProcess");
		
		from("direct:" + "KAD_CUSTOMER_EntityMultiKeyEntity_CustomerBusiness")
		.routeId("KAD_CUSTOMER_EntityMultiKeyEntity_CustomerBusiness")
 //   	.process(EntityConsumerInitilizer.PROCESS_ID)		
		.process("customerBusinessProcess");
	}
}
