package jp.co.acom.riza.utils.kafka;

/**
 * KAFKAユーティリティのコンスタント定義
 * 
 * @author developer
 */
public class KafkaConstants {

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
	 * ファイル受信イベントのコミットインターバル(ミリ秒)
	 */
	public static final int KAFKA_FILE_COMMIT_INTERVAL = 5000;
	/**
	 * ファイル受信イベントのコンシューマ接続数
	 */
	// public static final int KAFKA_FILE_CONNECT_COUNT = 1;
	/**
	 * ファイル受信イベントの同時コンシューマ数
	 */
	// public static final int KAFKA_FILE_CONSUMER_STREAMS = 10;
	/**
	 * ファイル受信コンシューマのスレッドプールサイズ
	 */
	// public static final int KAFKA_FILE_CONSUMER_POOLSIZE = 20;
	/**
	 * ファイル受信コンシューマの最大スレッドプールサイズ
	 */
	// public static final int KAFKA_FILE_CONSUMER_MAX_POOLSIZE = 50;
	/**
	 * ファイル受信コンシューマのスレッドプールのキープアライブ時間
	 */
	// public static final long KAFKA_FILE_CONSUMER_KEEP_ALIVE_TIME = 60;
	/**
	 * ファイル受信コンシューマのスレッドプールのID
	 */
	public static final String KAFKA_FILE_CONSUMER_POOL_ID = "ThreadPoolProfile";
}
