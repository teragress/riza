package jp.co.acom.riza.utils.context;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Configurable;

import jp.co.acom.riza.utils.kafka.KafkaMessageSendUtil;

/**
 * スレッドローカルのコンテキスト情報を管理する
 * 
 * @author developer
 */
@Configurable
public class RizaContext {
	/**
	 * KAFKAメッセージ送信ユーテリィのキー
	 */
	private static final String KAFKA_MESSAGE_SEND_UTIL = "KAFKA_MESSAGE_SEND_UTIL";
	/**
	 * ロールバック有無のキー
	 */
	private static final String TRANSACTION_ROLLBACK = "TRANSACTION_ROLLBACK";

	/**
	 * コンテキスト情報を格納するMap
	 */
	private static ThreadLocal<HashMap<String, Object>> threadLocalMap = new ThreadLocal<HashMap<String, Object>>() {

		@Override
		protected HashMap<String, Object> initialValue() {
			// TODO Auto-generated method stub
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(TRANSACTION_ROLLBACK, false);
			return map;
		}
	};

	/**
	 * KAFKAメッセージ送信ユーティリティを取得
	 * 
	 * @return
	 */
	public static KafkaMessageSendUtil getKafkaMessageSendUtil() {
		if (threadLocalMap.get().get(KAFKA_MESSAGE_SEND_UTIL) == null) {
			threadLocalMap.get().put(KAFKA_MESSAGE_SEND_UTIL, new KafkaMessageSendUtil());
		}
		return (KafkaMessageSendUtil) threadLocalMap.get().get(KAFKA_MESSAGE_SEND_UTIL);

	}

	/**
	 * ロールバック有無を返却する
	 * 
	 * @return ロールバック有無(true/false)
	 */
	public static Boolean isRollBack() {
		return (Boolean) threadLocalMap.get().get(TRANSACTION_ROLLBACK);
	}

	/**
	 * ロールバック有無を設定する
	 * 
	 * @param rollback ロールバック有無
	 */
	public static void setRollBack(Boolean rollback) {
		threadLocalMap.get().put(TRANSACTION_ROLLBACK, rollback);
	}

	/**
	 * コンテキストを削除する
	 */
	public static void removeRizaContext() {
		if (threadLocalMap != null) {
			threadLocalMap.remove();
		}
	}
}
