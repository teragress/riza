package jp.co.acom.riza.utils.kafka;

import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaManualCommit;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import jp.co.acom.riza.utils.log.Logger;

/**
 * @author developer
 *
 */
public class KafkaConfigUtil {

	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(KafkaConfigUtil.class);

	/**
	 * 
	 */
	public static KafkaConfiguration createKafkaConfiguration() {
		logger.debug("createKafkaConfiguration() start");
		KafkaConfiguration kafkaConfig = new KafkaConfiguration();
		kafkaConfig.setBrokers(KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_BOOTSTRAP_SERVER));
		kafkaConfig.setSecurityProtocol(KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_SECURITY_PROTOCOL));

		kafkaConfig.setSslKeystoreLocation(KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_KEYSTORE_LOCATION));
		kafkaConfig.setSslKeystoreType(KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_KEYSTORE_TYPE));
		kafkaConfig.setSslKeystorePassword(KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_KEYSTORE_PASSWORD));

		kafkaConfig.setSslTruststoreLocation(KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_TRUSTSTORE_LOCATION));
		kafkaConfig.setSslTruststoreType(KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_TRUSTSTORE_TYPE));
		kafkaConfig.setSslTruststorePassword(KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_TRUSTSTORE_PASSWORD));

	//	kafkaConfig.setAutoCommitEnable(false);
	//	kafkaConfig.setAllowManualCommit(true);
		// kafkaConfig.setAutoCommitIntervalMs(KafkaUtilConstants.KAFKA_FILE_COMMIT_INTERVAL);
	//	kafkaConfig.setAutoOffsetReset("earliest");
		kafkaConfig.setRequestRequiredAcks("all");
		kafkaConfig.setConsumersCount(KafkaUtilConstants.KAFKA_DEFAULT_CONSUMER_COUNT);
		kafkaConfig.setConsumerStreams(KafkaUtilConstants.KAFKA_DEFAULT_CONSUMER_STREAMS);

		System.out.println("Brokers:" + kafkaConfig.getBrokers());
		return kafkaConfig;
	}

	/**
	 * @return
	 */
	private static Properties createKafkaCommmonProperties() {

		Properties props = new Properties();
		props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
				KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_BOOTSTRAP_SERVER));
		props.put("security.protocol", KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_SECURITY_PROTOCOL));

		props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG,
				KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_KEYSTORE_LOCATION));
		props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG,
				KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_KEYSTORE_PASSWORD));
		props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_KEYSTORE_TYPE));
		props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
				KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_TRUSTSTORE_LOCATION));
		props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
				KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_TRUSTSTORE_PASSWORD));
		props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG,
				KafkaConfigUtil.getKafkaEnv(KafkaUtilConstants.KAFKA_TRUSTSTORE_TYPE));
		return props;
	}

	/**
	 * 環境変数を取得する
	 * 
	 * @param key キー
	 * @return 環境変数値
	 */
	public static String getKafkaEnv(String key) {
		String value = System.getenv(key);
		if (value == null) {
			value = System.getProperty(key);
		}
		if (value == null) {
			value = "";
		}
		logger.debug("getKafkaEnv() key=" + key + " value=" + value);

		return value;
	}
	
	public static int getIntegerEnv(String key,int defaultValue) {
		int retValue = defaultValue;
		try {
			retValue = Integer.parseInt(KafkaConfigUtil.getKafkaEnv(key));
			System.out.println("**** key=" + key + " retValue=" + retValue);
		} catch (NumberFormatException ex) {
			
		}
		return retValue;
		
	}
}
