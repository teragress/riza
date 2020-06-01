package jp.co.acom.renove.kafka.event;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.builder.ThreadPoolProfileBuilder;
import org.apache.camel.spi.ThreadPoolProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jp.co.acom.riza.utils.kafka.KafkaConfigUtil;
import jp.co.acom.riza.utils.kafka.KafkaUtilConstants;

@Configuration
public class EventThreadPoolSetting {
	@Bean(KafkaUtilConstants.EVENT_THREAD_POOL_BEAN)
	public ThreadPoolProfile createThreadPoolProfile() {

		ThreadPoolProfile consumer = 
				new ThreadPoolProfileBuilder(KafkaUtilConstants.EVENT_THREAD_POOL_ID)
				.poolSize(KafkaConfigUtil.getIntegerEnv(KafkaUtilConstants.EVENT_THREAD_POOL_SIZE,
						KafkaUtilConstants.EVENT_DEFAULT_THREAD_POOL_SIZE))
				.maxPoolSize(KafkaConfigUtil.getIntegerEnv(KafkaUtilConstants.EVENT_THREAD_MAX_POOL_SIZE,
						KafkaUtilConstants.EVENT_DEFAULT_THREAD_MAX_POOL_SIZE))
				.maxQueueSize(KafkaConfigUtil.getIntegerEnv(KafkaUtilConstants.EVENT_THREAD_MAX_QUE_SIZE,
						KafkaUtilConstants.EVENT_DEFAULT_THREAD_MAX_QUE_SIZE)) 
				.keepAliveTime(60L)
				.rejectedPolicy(ThreadPoolRejectedPolicy.CallerRuns)
				.build();
		System.out.println("**************************************************************************");
		System.out.println("**************************************************************************");
		System.out.println("**********Event Thread Pool Info******************************************");
		System.out.println(consumer.toString());
		System.out.println("**************************************************************************");
		System.out.println("**************************************************************************");
		return consumer;
	}
}

