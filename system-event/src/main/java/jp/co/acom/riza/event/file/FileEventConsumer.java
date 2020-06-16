package jp.co.acom.riza.event.file;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.kafka.KafkaConstants;
import jp.co.acom.riza.event.kafka.ManualCommitProcess;
import jp.co.acom.riza.utils.log.Logger;

@Component
public class FileEventConsumer extends RouteBuilder {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(FileEventConsumer.class);

	@Autowired
	Environment env;

	@Autowired
	FileEventProcess fileEventProcess;

	/**
	 * ファイル受信イベントの定義
	 */
	@Override
	public void configure() throws Exception {
		int consumerCount = env.getProperty(KafkaConstants.KAFKA_FILE_CONSUMER_COUNT, Integer.class,
				KafkaConstants.KAFKA_DEFAULT_FILE_CONSUMER_COUNT);

		logger.debug("configure() start");
		from("kafka:" + KafkaConstants.KAFKA_FILE_EVENT_TOPIC + "?groupId=" + KafkaConstants.KAFKA_FILE_EVENT_GROUP
				+ "&consumerCount=" + consumerCount)
						.routeId("kafka_file_consumer")
						.process(fileEventProcess)
						.process(new ManualCommitProcess());
	}
}
