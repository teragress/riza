package jp.co.acom.renove.kafka.event;

import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import jp.co.acom.riza.utils.kafka.KafkaConfigUtil;
import jp.co.acom.riza.utils.kafka.KafkaUtilConstants;

/**
 * KAFKAコンポーネントの登録クラス
 * 
 * @author developer
 *
 */
@Configuration
@Controller("SystemUtilKafkaComponentSetting")
public class KafkaComponentSetting {
	/**
	 * KAFKAコンシューマー用コンポーネントの登録メソッド
	 * 
	 * @return KAFKAコンポーネント
	 */
	@Bean(KafkaUtilConstants.KAFKA_SYSTEM_CONSUMER_BEAN_NAME)
	public static KafkaComponent createKafkaConsumerComponent() {
		// System.out.println("createKafkaComponent() start");
		KafkaConfiguration kafkaConfig = KafkaConfigUtil.createKafkaConfiguration();
		kafkaConfig.setGroupId(KafkaUtilConstants.KAFKA_FILE_EVENT_GROUP);
		KafkaComponent kafka = new KafkaComponent();
		kafka.setConfiguration(kafkaConfig);
		kafka.setAllowManualCommit(true);
		return kafka;
	}

	/**
	 * KAFKAプロデューサー用コンポーネントの登録メソッド
	 * 
	 * @return KAFKAコンポーネント
	 */
	@Bean(KafkaUtilConstants.KAFKA_SYSTEM_PRODUCER_BEAN_NAME)
	public static KafkaComponent createKafkaProducerComponent() {
		// System.out.println("createKafkaComponent() start");
		KafkaConfiguration kafkaConfig = KafkaConfigUtil.createKafkaConfiguration();
		KafkaComponent kafka = new KafkaComponent();
		kafka.setConfiguration(kafkaConfig);
		return kafka;
	}
}
