package jp.co.acom.riza.event.file;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import jp.co.acom.riza.event.kafka.KafkaConstants;
import jp.co.acom.riza.event.kafka.ManualCommitProcess;
import jp.co.acom.riza.system.utils.log.Logger;

//@Component
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
		int consumersCount = env.getProperty(KafkaConstants.KAFKA_FILE_CONSUMER_COUNT, Integer.class,
				KafkaConstants.KAFKA_DEFAULT_FILE_CONSUMER_COUNT);

		Boolean autoStart = env.getProperty(KafkaConstants.KAFKA_MOCK,Boolean.class,true);
		
		logger.debug("configure() start");
		from("kafka:" + KafkaConstants.KAFKA_FILE_EVENT_TOPIC + "?groupId=" + KafkaConstants.KAFKA_FILE_EVENT_GROUP
				+ "&consumersCount=" + consumersCount)
						.autoStartup(autoStart)
						.routeId("kafka_file_consumer")
						.process(fileEventProcess)
						.process(new ManualCommitProcess());
	}
}
