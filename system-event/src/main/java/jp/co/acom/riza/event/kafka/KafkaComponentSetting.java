package jp.co.acom.riza.event.kafka;

import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import jp.co.acom.riza.utils.log.Logger;

/**
 * KAFKAコンポーネントの登録クラス
 *
 * @author developer
 *
 */
@Configuration
//@Controller("SystemUtilKafkaComponentSetting")
public class KafkaComponentSetting {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(KafkaComponentSetting.class);

	@Autowired
	private Environment env;
	/**
	 * KAFKAコンシューマー用コンポーネントの登録メソッド
	 *
	 * @return KAFKAコンポーネント
	 */
	@Bean(KafkaConstants.KAFKA_COMPONENT_BEAN)
	public KafkaComponent createKafkaConsumerComponent() {
		// System.out.println("createKafkaComponent() start");
		KafkaConfiguration kafkaConfig = createKafkaConfiguration();
		//kafkaConfig.setGroupId(KafkaConstants.KAFKA_FILE_EVENT_GROUP);
		kafkaConfig.setAutoCommitEnable(false);
		kafkaConfig.setAllowManualCommit(true);
		KafkaComponent kafka = new KafkaComponent();
		kafka.setConfiguration(kafkaConfig);
		kafka.setAllowManualCommit(true);
		return kafka;
	}

	/**
	 *
	 */
	private KafkaConfiguration createKafkaConfiguration() {
		logger.debug("createKafkaConfiguration() start");
		KafkaConfiguration kafkaConfig = new KafkaConfiguration();
		kafkaConfig.setBrokers(env.getProperty(KafkaConstants.KAFKA_BOOTSTRAP_SERVER));
		kafkaConfig.setSecurityProtocol(env.getProperty(KafkaConstants.KAFKA_SECURITY_PROTOCOL));
		kafkaConfig.setSslKeystoreLocation(env.getProperty(KafkaConstants.KAFKA_KEYSTORE_LOCATION));
		kafkaConfig.setSslKeystoreType(env.getProperty(KafkaConstants.KAFKA_KEYSTORE_TYPE));
		kafkaConfig.setSslKeystorePassword(env.getProperty(KafkaConstants.KAFKA_KEYSTORE_PASSWORD));
		kafkaConfig.setSslTruststoreLocation(env.getProperty(KafkaConstants.KAFKA_TRUSTSTORE_LOCATION));
		kafkaConfig.setSslTruststoreType(env.getProperty(KafkaConstants.KAFKA_TRUSTSTORE_TYPE));
		kafkaConfig.setSslTruststorePassword(env.getProperty(KafkaConstants.KAFKA_TRUSTSTORE_PASSWORD));
		kafkaConfig.setRequestRequiredAcks(
				env.getProperty(KafkaConstants.KAFKA_REQUIRED_ACKS, KafkaConstants.KAFKA_DEFAULT_REQUIRED_ACKS));
		kafkaConfig.setConsumerStreams(1);

//		System.out.println("Brokers:" + kafkaConfig.getBrokers());
		return kafkaConfig;
	}
}
