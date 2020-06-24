package jp.co.acom.riza.event.kafka;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Configuration;
/**
 * @author vagrant
 *
 */
//@Configuration
public class KafkaDynamicRouteBuilder extends RouteBuilder {
	/**
	 *
	 */
	@Override
	public void configure() throws Exception {
		from("direct:kafkaProducer")
		.id("kafkaDynamicProducer")
		.toD(KafkaConstants.KAFKA_COMPONENT_BEAN + ":${topicName}");
	}
}