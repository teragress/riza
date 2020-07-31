package jp.co.acom.riza.event.file;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import jp.co.acom.riza.event.kafka.KafkaConstants;
import jp.co.acom.riza.event.kafka.ManualCommitProcess;
import jp.co.acom.riza.event.utils.ModeUtil;
import jp.co.acom.riza.system.utils.log.Logger;

@Configuration
public class FileEventConsumer extends RouteBuilder {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(FileEventConsumer.class);

	@Autowired
	Environment env;

	@Autowired
	FileEventProcess fileEventProcess;

	@Autowired
	ApplicationContext applicationContext;
	
	/**
	 * ファイル受信イベントの定義
	 */
	@Override
	public void configure() throws Exception {
		logger.debug("configure() start");
		
		if (ModeUtil.isOnline() || ModeUtil.isKafkaMock()) {
			return;
		}
		
		int consumersCount = env.getProperty(KafkaConstants.KAFKA_FILE_CONSUMER_COUNT, Integer.class,
				KafkaConstants.KAFKA_DEFAULT_FILE_CONSUMER_COUNT);

		from("kafka:" + KafkaConstants.KAFKA_FILE_EVENT_TOPIC + "?groupId=" + KafkaConstants.KAFKA_FILE_EVENT_GROUP
				+ "&consumersCount=" + consumersCount)
						.routeId("kafka_file_consumer")
						.process(fileEventProcess)
						.process(ManualCommitProcess.PROCESS_ID);
	}
}
