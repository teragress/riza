package jp.co.acom.riza.config;

import java.util.concurrent.ExecutorService;

import org.apache.camel.CamelContext;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.builder.ThreadPoolBuilder;
import org.apache.camel.builder.ThreadPoolProfileBuilder;
import org.apache.camel.spi.ExecutorServiceManager;
import org.apache.camel.spi.ThreadPoolProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class EventThreadPoolSetting {

	@Autowired
	private Environment env;
	
	@Autowired
	CamelContext context;

	@Bean(EventConstants.EVENT_THREAD_POOL_BEAN)
	public ExecutorService createExecutorService() throws Exception {
		ThreadPoolBuilder builder = new ThreadPoolBuilder(context);
		ExecutorService executorService = builder
						.poolSize(env.getProperty(EventConstants.EVENT_THREAD_POOL_SIZE, Integer.class,
						EventConstants.EVENT_DEFAULT_THREAD_POOL_SIZE))
				.maxPoolSize(env.getProperty(EventConstants.EVENT_THREAD_MAX_POOL_SIZE, Integer.class,
						EventConstants.EVENT_DEFAULT_THREAD_MAX_POOL_SIZE))
				.maxQueueSize(env.getProperty(EventConstants.EVENT_THREAD_MAX_QUE_SIZE, Integer.class,
						EventConstants.EVENT_DEFAULT_THREAD_MAX_QUE_SIZE))
				.keepAliveTime(60L)
				.rejectedPolicy(ThreadPoolRejectedPolicy.CallerRuns)
				.build(EventConstants.EVENT_THREAD_POOL_ID);
		System.out.println("**************************************************************************");
		System.out.println("**************************************************************************");
		System.out.println("**********Event Thread Pool Info******************************************");
		System.out.println(executorService.toString());
		System.out.println("**************************************************************************");
		System.out.println("**************************************************************************");
		return executorService;
	}
}
