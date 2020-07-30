package jp.co.acom.riza.event.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.event.config.EventConstants;

/**
 * コンシューマグループ、トピック、アプリケーションルートの関係を保持する
 * 
 * @author teratani
 *
 */
@Component
public class ApplicationRouteHolder {
	public enum EventType {
		DOMAIN, ENTITY
	}

	/**
	 * ロガー
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRouteHolder.class);

	/**
	 * トピックルートホルダー
	 */
	private static Map<String, Map<String, List<String>>> rootHolder = new HashMap<String, Map<String, List<String>>>();

	/**
	 * コンシューマトピックホルダー
	 */
	private static Map<String, EventType> topicHolder = new HashMap<String, ApplicationRouteHolder.EventType>();

	/**
	 * システムプロパティ環境変数取得用
	 */
	@Autowired
	private Environment env;

	/**
	 * イベント起動モード
	 */
	boolean createConsumer = true;

	@PostConstruct
	public void initilize() {
		if (!EventConstants.EVENT_DEFAULT_START_MODE
				.equals(env.getProperty(EventConstants.EVENT_START_MODE, EventConstants.EVENT_DEFAULT_START_MODE))) {
			createConsumer = false;
		}
		if (env.getProperty(KafkaConstants.KAFKA_MOCK, Boolean.class, false)) {
			createConsumer = false;
		}
	}

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
		if (createConsumer) {
			if (rootHolder.get(consumerGroup) == null) {
				rootHolder.put(consumerGroup, new HashMap<String, List<String>>());
			}
			if (rootHolder.get(consumerGroup).get(topic) == null) {
				rootHolder.get(consumerGroup).put(topic, new ArrayList<String>());
			}
			rootHolder.get(consumerGroup).get(topic).add(applicationRoute);
		}
		topicHolder.put(topic, eventType);
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

	/**
	 * コンシューマトピック情報を返す
	 * 
	 * @param topic
	 * @return イベントタイプ(nullは登録なし)
	 */
	public static EventType getTopic(String topic) {
		return topicHolder.get(topic);
	}
}
