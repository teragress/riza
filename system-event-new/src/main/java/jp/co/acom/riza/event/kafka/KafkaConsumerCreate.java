package jp.co.acom.riza.event.kafka;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.config.EventConstants;
import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

/**
 * 起動されているビジネス用コンシューマからKAFKAのコンシューマを作成する
 *
 * @author teratani
 *
 */
@Component
public class KafkaConsumerCreate {

	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(KafkaConsumerCreate.class);

	@Autowired
	private Environment env;
	/**
	 * Camelコンテキスト
	 */

	@Autowired
	ApplicationContext aplContext;
	/**
	 *
	 */
	@Autowired
	private AppRouteHolder holder;

	/**
	 * アプリケーションルートの設定
	 * 
	 * @param context アプリケーションコンテキスト
	 */
	public void setApplicationRouteHolder(CamelContext context) {
		logger.debug("setApplicationrouteHolder() started.");

		Collection<RouteDefinition> routes = context.getRouteDefinitions();
		for (RouteDefinition root : routes) {
			String[] splitStr = root.getId().split("_", 4);
			if (splitStr.length == 4 && KafkaConstants.KAFKA_ENTITY_APL_ROUTE_PREFIX.equals(splitStr[0])) {
				logger.info("holder add routeid=" + root.getId());
				holder.addApplicationRoute(splitStr[1], splitStr[2], root.getId(),AppRouteHolder.EventType.ENTITY);
			}
			if (splitStr.length == 4 && KafkaConstants.KAFKA_DOMAIN_APL_ROUTE_PREFIX.equals(splitStr[0])) {
				logger.info("holder add routeid=" + root.getId());
				holder.addApplicationRoute(splitStr[1], splitStr[2], root.getId(),AppRouteHolder.EventType.DOMAIN);
			}
		}
	}

	/**
	 * Kafkaコンシューマの作成
	 * 
	 * @throws Exception
	 */
	public void createConsumer(CamelContext context) throws Exception {
		logger.debug("createConsumer() started.");

		int defaultConsumerCount = env.getProperty(KafkaConstants.KAFKA_DEFAULT_CONSUMER_COUNT, Integer.class,
				KafkaConstants.KAFKA_DEFAULT_PROG_CONSUMER_COUNT);
		for (String group : holder.getGroupeList()) {
			String topicList = String.join(",", holder.getTopicList(group));
			String key = "KAFKA_" + group + "_CONSUMER_COUNT";
			int consumersCount = env.getProperty(key, Integer.class, defaultConsumerCount);
			String routeId = KafkaConstants.KAFKA_CONSUMER_PREFIX + "_" + group;
			String uri = KafkaConstants.KAFKA_COMPONENT_BEAN + ":" + topicList + "?groupId=" + group + "&consumersCount=" + consumersCount;

			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from(uri).routeId(routeId).threads()
							.executorService(
									(ExecutorService) aplContext.getBean(EventConstants.EVENT_THREAD_POOL_BEAN))
							.doTry()
								.process(DynamicExecuteProcess.PROCESS_ID)
							.doFinally()
								.process(ManualCommitProcess.PROCESS_ID)
							.end();
				}
			});
			logger.info(MessageFormat.getMessage(EventMessageId.KAFKA_CONSUMER_CREATE, routeId, uri,consumersCount));
		}
	}
}
