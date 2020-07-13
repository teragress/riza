package jp.co.acom.riza.event.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.kafka.EntityConsumerInitilizer;
import jp.co.acom.riza.exception.DuplicateExecuteException;

@Component
public class RouteIntercepterConfiguration extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		interceptFrom("direct:KAD_CUSTOMER_EntityCustomer_CustomerBusiness")
		.doTry()
	    	.process(EntityConsumerInitilizer.PROCESS_ID)
	    .doCatch(DuplicateExecuteException.class)
	    	.stop()
	    .end();
	}	
}
