package jp.co.acom.riza.event.config;

import java.util.concurrent.ExecutorService;

import org.apache.camel.CamelContext;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.builder.ThreadPoolBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

@Configuration
public class EventThreadPoolSetting {

	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(EventThreadPoolSetting.class);
	
	@Autowired
	private Environment env;

	@Autowired
	CamelContext context;

	@Bean(EventConstants.EVENT_THREAD_POOL_BEAN)
	public ExecutorService createExecutorService() throws Exception {
		ThreadPoolBuilder builder = new ThreadPoolBuilder(context);

		Integer poolSize = env.getProperty(EventConstants.EVENT_THREAD_POOL_SIZE, Integer.class,
				EventConstants.EVENT_DEFAULT_THREAD_POOL_SIZE);
		Integer maxPoolSize = env.getProperty(EventConstants.EVENT_THREAD_MAX_POOL_SIZE, Integer.class,
				EventConstants.EVENT_DEFAULT_THREAD_MAX_POOL_SIZE);
		Integer maxQueueSize = env.getProperty(EventConstants.EVENT_THREAD_MAX_QUE_SIZE, Integer.class,
				EventConstants.EVENT_DEFAULT_THREAD_MAX_QUE_SIZE);
		ExecutorService executorService = builder
				.poolSize(poolSize)
				.maxPoolSize(maxPoolSize)
				.maxQueueSize(maxQueueSize)
				.keepAliveTime(60L).rejectedPolicy(ThreadPoolRejectedPolicy.CallerRuns)
				.build(EventConstants.EVENT_THREAD_POOL_ID);
		logger.info(MessageFormat.get(EventMessageId.CONSUMER_THREAD_POOL),poolSize,maxPoolSize,maxQueueSize);
		return executorService;
	}
}
