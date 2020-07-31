package jp.co.acom.riza.event.report;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.kafka.KafkaConstants;
import jp.co.acom.riza.event.kafka.ManualCommitProcess;
import jp.co.acom.riza.event.utils.ModeUtil;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * 帳票向けコンシューマ
 * @author vagrant
 *
 */
@Component
public class ReportConsumer extends RouteBuilder {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(ReportConsumer.class);
	
	public static final String REPORT_CONSUMER_ROUTE_ID = "report_output_consumer";

	@Autowired
	Environment env;

	/**
	 * 帳票コンシューマ定義
	 */
	@Override
	public void configure() throws Exception {
		logger.debug("configure() start");

		if (ModeUtil.isKafkaMock() || !ModeUtil.isReportOutput()) {
			return;
		};

		String topicName = env.getProperty(KafkaConstants.KAFKA_REPORT_TOPIC,KafkaConstants.KAFKA_DEFAULT_REPORT_TOPIC);

		from(KafkaConstants.KAFKA_COMPONENT_BEAN + ":" + topicName + "?groupId="
				+ KafkaConstants.KAFKA_REPORT_TOPIC_GROUP + "&consumersCount="
				+ KafkaConstants.KAFKA_REPORT_CONSUMER_COUNT)
					.routeId(REPORT_CONSUMER_ROUTE_ID)
					.doTry()
						.to(ReportMessagePut.PROCESS_ID)
						.process(ManualCommitProcess.PROCESS_ID)
					.doCatch(Exception.class)
						.process(ReportConsumerStop.PROCESS_ID)
					.end();
	}
}
