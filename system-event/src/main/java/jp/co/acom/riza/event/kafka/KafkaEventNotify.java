package jp.co.acom.riza.event.kafka;

import java.util.EventObject;
import org.apache.camel.management.event.CamelContextStartedEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jp.co.acom.riza.utils.log.Logger;

/**
 * 起動されているビジネス用コンシューマからKAFKAのコンシューマを作成する
 *
 * @author developer
 *
 */

@Component
public class KafkaEventNotify extends EventNotifierSupport {

	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(KafkaEventNotify.class);

	@Autowired
	KafkaConsumerCreate consumerCreate;
	/**
	 *　Camelコンテキスト
	 */

	/**
	 * CamelcontextStartedEventトリガー
	 */
	@Override
	public void notify(EventObject event) throws Exception {
		logger.info("notify() started.");
		if (event instanceof CamelContextStartedEvent) {
			CamelContextStartedEvent startEvent = (CamelContextStartedEvent)event;
			consumerCreate.setContext(startEvent.getContext());
			consumerCreate.setApplicationRouteHolder();
			consumerCreate.createConsumer();
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
}
