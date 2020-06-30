package jp.co.acom.riza.event.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * コンシューマグループ、トピック、アプリケーションルートの関係を保持する
 * @author mtera1003
 *
 */
@Component
//@Scope("singleton")
public class ApplicationRouteHolder {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRouteHolder.class);

	private static Map<String, Map<String, List<String>>> holder = new HashMap<String, Map<String, List<String>>>();

	/**
	 * アプリケーションルートを登録する
	 */
	public void addApplicationRoute(String consumerGroup, String topic, String applicationRoute) {
		LOGGER.debug("addAppoicationRoute() consumerGroup=" + consumerGroup + " topic=" + topic +
				"applicationroute=" + applicationRoute);
		if (holder.get(consumerGroup) == null) {
			holder.put(consumerGroup, new HashMap<String, List<String>>());
		}
		if (holder.get(consumerGroup).get(topic) == null) {
			holder.get(consumerGroup).put(topic, new ArrayList<String>());
		}
		holder.get(consumerGroup).get(topic).add(applicationRoute);
	}

	/**
	 * 実行するアプリケーションルートリストを取得する
	* @param consumerGroup　コンシューマグループ
	* @param topic　トピック
	* @return　アプリケーションルートリスト
	*/
	public List<String> getApplicationRoutes(String consumerGroup, String topic) {
		return holder.get(consumerGroup).get(topic);
	}

	/**
	 * コンシューマグループリストを取得する
	 * @return コンシューマグループリスト
	 */
	public List<String> getGroupeList() {
		return new ArrayList<>(holder.keySet());
	}

	/**
	 * 指定されたコンシューマグループのトピックリストを取得する
	 * @param consumerGroup コンシューマグループ
	 * @return トピックスリスト
	 */
	public List<String> getTopicList(String consumerGroup) {
		return new ArrayList<>(holder.get(consumerGroup).keySet());
	}
}
