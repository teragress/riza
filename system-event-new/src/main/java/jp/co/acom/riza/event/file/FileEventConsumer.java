package jp.co.acom.riza.event.file;

import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.config.EventConstants;
import jp.co.acom.riza.event.kafka.DynamicExecuteProcess;
import jp.co.acom.riza.event.kafka.KafkaConstants;
import jp.co.acom.riza.event.kafka.ManualCommitProcess;
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
	 * イベント起動モード
	 */
	boolean consumerCreate = true;
	
	/**
	 * ファイル受信イベントの定義
	 */
	@Override
	public void configure() throws Exception {
		logger.debug("configure() start");
		if (!EventConstants.EVENT_DEFAULT_START_MODE
				.equals(env.getProperty(EventConstants.EVENT_START_MODE, EventConstants.EVENT_DEFAULT_START_MODE))) {
			return;
		}
		if (env.getProperty(KafkaConstants.KAFKA_MOCK,Boolean.class,false)) {
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
