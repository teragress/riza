package jp.co.acom.renove.kafka.event;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import jp.co.acom.riza.utils.kafka.KafkaUtilConstants;
import jp.co.acom.riza.utils.log.Logger;

@Component
public class SystemCommonProducer extends RouteBuilder {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(SystemCommonProducer.class);
	
	/**
	 * システム共通のプロデューサー定義
	 */
	@Override
	public void configure() throws Exception {
		logger.debug("configure() start");
		from("direct:RIZA_MAX.AUTH.DNLD")
       .log("MAX.AUTH.DNLD = ${body}")
       .to(KafkaUtilConstants.KAFKA_SYSTEM_PRODUCER_BEAN_NAME + ":RIZA_MAX.AUTH.DNLD")
       ;
	}
}
