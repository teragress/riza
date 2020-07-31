package jp.co.acom.riza.event.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.utils.ModeUtil;

/**
 * コンシューマグループ、トピック、アプリケーションルートの関係を保持する
 * 
 * @author teratani
 *
 */
@Component
public class AppRouteHolder {
	public enum EventType {
		DOMAIN, ENTITY
	}

	/**
	 * ロガー
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AppRouteHolder.class);

	/**
	 * トピックルートホルダー
	 */
	private static Map<String, Map<String, List<String>>> rootHolder = new HashMap<String, Map<String, List<String>>>();

	/**
	 * アプリケーションルートを登録する
	 * 
	 * @param consumerGroup    コンシューマグループ
	 * @param topic            トピック
	 * @param applicationRoute アプリケーションルートID
	 */
	public void addApplicationRoute(String consumerGroup, String topic, String applicationRoute, EventType eventType) {
		LOGGER.debug("addAppoicationRoute() consumerGroup=" + consumerGroup + " topic=" + topic + "applicationroute="
				+ applicationRoute);
		if (rootHolder.get(consumerGroup) == null) {
			rootHolder.put(consumerGroup, new HashMap<String, List<String>>());
		}
		if (rootHolder.get(consumerGroup).get(topic) == null) {
			rootHolder.get(consumerGroup).put(topic, new ArrayList<String>());
		}
		rootHolder.get(consumerGroup).get(topic).add(applicationRoute);
	}

	/**
	 * 実行するアプリケーションルートリストを取得する
	 * 
	 * @param consumerGroup コンシューマグループ
	 * @param topic         トピック
	 * @return アプリケーションルートリスト
	 */
	public List<String> getApplicationRoutes(String consumerGroup, String topic) {
		return rootHolder.get(consumerGroup).get(topic);
	}

	/**
	 * コンシューマグループリストを取得する
	 * 
	 * @return コンシューマグループリスト
	 */
	public List<String> getGroupeList() {
		return new ArrayList<>(rootHolder.keySet());
	}

	/**
	 * 指定されたコンシューマグループのトピックリストを取得する
	 * 
	 * @param consumerGroup コンシューマグループ
	 * @return トピックスリスト
	 */
	public List<String> getTopicList(String consumerGroup) {
		return new ArrayList<>(rootHolder.get(consumerGroup).keySet());
	}
}
