package jp.co.acom.riza.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.kafka.EntityConsumerInitilizer;
import jp.co.acom.riza.event.kafka.KafkaConstants;
import jp.co.acom.riza.exception.DuplicateExecuteException;

@Component
public class RouteIntercepterConfiguration extends RouteBuilder {
	
	@Override
//	@Transactional(rollbackOn = Throwable.class)
	public void configure() throws Exception {
		interceptFrom(KafkaConstants.KAFKA_APPLICATION_ROUTE_PREFIX + "_*")
		.transacted()
		.doTry()
	    	.process(EntityConsumerInitilizer.PROCESS_ID)
	    .doCatch(DuplicateExecuteException.class)
	    	.stop()
	    .end();
	}	
}
