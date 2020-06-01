package jp.co.acom.renove.kafka.event;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.utils.kafka.KafkaUtilConstants;
import jp.co.acom.riza.utils.kafka.ManualCommitProcess;
import jp.co.acom.riza.utils.log.Logger;

@Component
public class FileEventConsumer extends RouteBuilder {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(FileEventConsumer.class);

	@Autowired
	FileEventProcess fileEventProcess;
	/**
	 * ファイル受信イベントの定義
	 */
	@Override
	public void configure() throws Exception {
		System.out.println("configure() start");
		logger.debug("configure() start");
		from(KafkaUtilConstants.KAFKA_SYSTEM_CONSUMER_BEAN_NAME + ":" + KafkaUtilConstants.KAFKA_FILE_EVENT_TOPIC)
       .routeId("kafka_file_consumer")
		.process(fileEventProcess)
		.process(new ManualCommitProcess())
		;
	}
}
