package jp.co.acom.riza.utils.kafka;

/**
 * KAFKAユーティリティのコンスタント定義
 * 
 * @author developer
 *
 */
public class KafkaUtilConstants {

	/**
	 * KAFKA接続サーバー
	 */
	public static final String KAFKA_BOOTSTRAP_SERVER = "KAFKA_BOOTSTRAP_SERVER";
	/**
	 * KafkaZookeeper接続サーバー
	 */
	public static final String KAFKA_ZOOKEEPER_SERVER = "KAFKA_ZOOKEEPER_SERVER";
	/**
	 * KAFKAセキュリティ接続プロトコル
	 */
	public static final String KAFKA_SECURITY_PROTOCOL = "KAFKA_SECURITY_PROTOCOL";
	/**
	 * KAFKAキーストアロケーション
	 */
	public static final String KAFKA_KEYSTORE_LOCATION = "KAFKA_KEYSTORE_LOCATION";
	/**
	 * KAFKAキーストアパスワード
	 */
	public static final String KAFKA_KEYSTORE_PASSWORD = "KAFKA_KEYSTORE_PASSWORD";
	/**
	 * KAFKAキーストアタイプ
	 */
	public static final String KAFKA_KEYSTORE_TYPE = "KAFKA_KEYSTORE_TYPE";
	/**
	 * KAFKAトラストストアロケーション
	 */
	public static final String KAFKA_TRUSTSTORE_LOCATION = "KAFKA_TRUSTSTORE_LOCATION";
	/**
	 * KAFKAトラストストアパスワード
	 */
	public static final String KAFKA_TRUSTSTORE_PASSWORD = "KAFKA_TRUSTSTORE_PASSWORD";
	/**
	 * KAFKAトラストストアタイプ
	 */
	public static final String KAFKA_TRUSTSTORE_TYPE = "KAFKA_TRUSTSTORE_TYPE";
	/**
	 * トピックプレフィックス
	 */
	public static final String KAFKA_TOPIC_PREFIX = "RIZA_";
	/**
	 * ファイル受信イベントのトピック名
	 */
	public static final String KAFKA_FILE_EVENT_TOPIC = "KAFKA_FILE_EVENT_TOPIC";
	/**
	 * ファイル受信イベントのコンシューマグループ名
	 */
	public static final String KAFKA_FILE_EVENT_GROUP = "KAFKA_FILE_EVENT_GROUP";
	/**
	 * 
	 */
	public static final String EVENT_THREAD_POOL_SIZE = "EVENT_THREAD_POOL_SIZE";
	/**
	 * 
	 */
	public static final String EVENT_THREAD_MAX_POOL_SIZE = "EVENT_THREAD_MAX_POOL_SIZE";
	/**
	 * 
	 */
	public static final int EVENT_DEFAULT_THREAD_POOL_SIZE = 50;
	/**
	 * 
	 */
	public static final int EVENT_DEFAULT_THREAD_MAX_POOL_SIZE = 50;
	/**
	 * 
	 */
	public static final String EVENT_THREAD_MAX_QUE_SIZE = "EVENT_THREAD_MAX_QUE_SIZE";
	/**
	 * 
	 */
	public static final int EVENT_DEFAULT_THREAD_MAX_QUE_SIZE = 5000;
	
	/**
	 * デフォルトのイベントのコンシューマ接続数
	 */
	public static final int KAFKA_DEFAULT_CONSUMER_COUNT = 1;
	/**
	 * デフォルトのイベントの同時コンシューマ数
	 */
	public static final int KAFKA_DEFAULT_CONSUMER_STREAMS = 1;
	/**
	 * 
	 */
	public static final String EVENT_THREAD_POOL_ID = "ThreadPoolProfile";
	/**
	 * 
	 */
	public static final String EVENT_THREAD_POOL_BEAN = "ThreadPoolBean";
	/**
	 * KAFKAコンポーネント設定用コンシューマBEAN名
	 */
	public static final String KAFKA_SYSTEM_CONSUMER_BEAN_NAME = "SystemConsumerBean";
	/**
	 * KAFKAコンポーネント設定用プロデューサーBEAN名
	 */
	public static final String KAFKA_SYSTEM_PRODUCER_BEAN_NAME = "SystemProducerBean";
}
