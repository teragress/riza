package jp.co.acom.riza.event.kafka;

import java.util.EventObject;
import org.apache.camel.CamelContext;
import org.apache.camel.management.event.CamelContextStartedEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.utils.ModeUtil;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * 起動されているビジネス用コンシューマからKAFKAのコンシューマを作成する
 *
 * @author teratani
 *
 */
@Component
public class CamelNotifyLintner extends EventNotifierSupport {

	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(CamelNotifyLintner.class);

	@Autowired
	KafkaConsumerCreate create;
	
	/**
	 * CamelcontextStartedEventトリガー<br>
	 * アプリケーション呼出用のホルダー作成処理の呼出し<br>
	 * KAFKAコンシューマの作成処理呼出し
	 */
	@Override
	public void notify(EventObject event) throws Exception {
		logger.debug("notify() started. ");
		
		if (ModeUtil.isKafkaMock() || ModeUtil.isOnline()) {
			return;
		}
		
		// CAMELの初期化完了イベント
		if (event instanceof CamelContextStartedEvent) {
			CamelContextStartedEvent startEvent = (CamelContextStartedEvent)event;
			CamelContext context = startEvent.getContext();
			create.setApplicationRouteHolder(context);
			create.createConsumer(context);
		}
	}

	/**
	 *　CamelContextStartedEventの有効化
	 */
	@Override
	public boolean isEnabled(EventObject event) {
		logger.debug("isEnabled() stared.");
		if (event instanceof CamelContextStartedEvent) {
			return true;
		}
		return false;
	}
}
