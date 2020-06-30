package jp.co.acom.riza.event.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
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
		System.out.println("***********************createKafkaComponent() start");
		KafkaConfiguration kafkaConfig = createKafkaConfiguration();
		// kafkaConfig.setGroupId(KafkaConstants.KAFKA_FILE_EVENT_GROUP);
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

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> configProps = new HashMap<String, Object>();

		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				env.getProperty(KafkaConstants.KAFKA_BOOTSTRAP_SERVER));
		configProps.put(SslConfigs.DEFAULT_SSL_PROTOCOL, 
				env.getProperty(KafkaConstants.KAFKA_SECURITY_PROTOCOL));
		configProps.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, 
				env.getProperty(KafkaConstants.KAFKA_KEYSTORE_TYPE));
		configProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,
				env.getProperty(KafkaConstants.KAFKA_KEYSTORE_LOCATION));
		configProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,
				env.getProperty(KafkaConstants.KAFKA_KEYSTORE_PASSWORD));
		configProps.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, 
				env.getProperty(KafkaConstants.KAFKA_TRUSTSTORE_TYPE));
		configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
				env.getProperty(KafkaConstants.KAFKA_TRUSTSTORE_LOCATION));
		configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
				env.getProperty(KafkaConstants.KAFKA_TRUSTSTORE_PASSWORD));

		configProps.put(ProducerConfig.ACKS_CONFIG,
				env.getProperty(KafkaConstants.KAFKA_REQUIRED_ACKS, KafkaConstants.KAFKA_DEFAULT_REQUIRED_ACKS));
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory(configProps);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
