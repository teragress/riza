package jp.co.acom.riza.event.kafka;

import java.util.Collection;
import java.util.EventObject;
import java.util.concurrent.ExecutorService;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.management.event.CamelContextStartedEvent;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.util.CamelContextResolverHelper;
import org.apache.camel.support.EventNotifierSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.config.EventConstants;
import jp.co.acom.riza.utils.log.Logger;

/**
 * 起動されているビジネス用コンシューマからKAFKAのコンシューマを作成する
 *
 * @author developer
 *
 */

@Component
public class KafkaConsumerCreate extends EventNotifierSupport {

	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(KafkaConsumerCreate.class);

	@Autowired
	private Environment env;
	/**
	 *　Camelコンテキスト
	 */
//	@Autowired
	private CamelContext context;
	
	public void setContext(CamelContext context) {
		this.context = context;
	}

	@Autowired
	ApplicationContext aplContext;
	/**
	 *
	 */
	@Autowired
	private ApplicationRouteHolder holder;

	/**
	 * CamelcontextStartedEventトリガー
	 */
	@Override
	public void notify(EventObject event) throws Exception {
		logger.info("notify() started.");
		if (event instanceof CamelContextStartedEvent) {
			CamelContextStartedEvent startEvent = (CamelContextStartedEvent)event;
			context = startEvent.getContext();
			setApplicationRouteHolder();
			createConsumer();
		}
	}

	/**
	 *　CamelContextStartedEventの有効化
	 */
	@Override
	public boolean isEnabled(EventObject event) {
		logger.info("isEnabled() stared.**********************************************************");
		if (event instanceof CamelContextStartedEvent) {
			return true;
		}
		return false;
	}
	/**
	 * アプリケーションルートの設定
	 */
	public void setApplicationRouteHolder() {
		logger.debug("setApplicationrouteHolder() started.");
		
		Collection<RouteDefinition> routes = context.getRouteDefinitions();
		for (RouteDefinition root : routes) {
			String[] splitStr = root.getId().split("_", 4);
			if (splitStr.length == 4 && KafkaConstants.KAFKA_APPLICATION_ROUTE_PREFIX.equals(splitStr[0])) {
				holder.addApplicationRoute(splitStr[1], splitStr[2], splitStr[3]);
			}
		}
	}

	/**
	 *　Kafkaコンシューマの作成
	 * @throws Exception
	 */
	public void createConsumer() throws Exception {
		logger.debug("createConsumer() started.");

		int defaultConsumerCount = env.getProperty(KafkaConstants.KAFKA_DEFAULT_CONSUMER_COUNT, Integer.class,
				KafkaConstants.KAFKA_DEFAULT_PROG_CONSUMER_COUNT);
		for (String group : holder.getGroupeList()) {
			String topicList = String.join(",", holder.getTopicList(group));
			String key = "KAFKA_" + group + "_CONSUMER_COUNT";
			int consumersCount = env.getProperty(key,Integer.class,defaultConsumerCount);
			String routeId = KafkaConstants.KAFKA_CONSUMER_PREFIX + "_" + group;
			logger.info("addRoutes routeId=" + routeId);
			
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("kafka:" + topicList + "?groupId=" + group + "&consumersCount=" + consumersCount)
							.routeId(routeId)
							.threads().executorService((ExecutorService)aplContext.getBean(EventConstants.EVENT_THREAD_POOL_BEAN))
							.process(new DynamicExecuteProcess())
							.process(new ManualCommitProcess());
				}
			});
		}
	}
}
