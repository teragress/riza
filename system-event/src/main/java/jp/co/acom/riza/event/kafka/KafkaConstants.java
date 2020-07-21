package jp.co.acom.riza.event.kafka;

/**
 * KAFKAユーティリティのコンスタント定義
 *
 * @author developer
 *
 */
public class KafkaConstants {

	/**
	 * KAFKAのbean名
	 */
	public static final String KAFKA_COMPONENT_BEAN = "kafka";
	
	/**
	 * KAFKAのエンティティイベントのトピックプレフィックス
	 */
	public static final String KAFKA_ENTITY_TOPIC_PREFIX = "Entity";
	
	/**
	 * KAFKA接続サーバー取得キー
	 */
	public static final String KAFKA_BOOTSTRAP_SERVER = "KAFKA_BOOTSTRAP_SERVER";
	
	/**
	 * KafkaZookeeper接続サーバ取得キー
	 */
	public static final String KAFKA_ZOOKEEPER_SERVER = "KAFKA_ZOOKEEPER_SERVER";
	
	/**
	 * KAFKAセキュリティ接続プロトコル取得キー
	 */
	public static final String KAFKA_SECURITY_PROTOCOL = "KAFKA_SECURITY_PROTOCOL";
	
	/**
	 * KAFKAキーストアロケーション取得キー
	 */
	public static final String KAFKA_KEYSTORE_LOCATION = "KAFKA_KEYSTORE_LOCATION";
	
	/**
	 * KAFKAキーストアパスワード取得キー
	 */
	public static final String KAFKA_KEYSTORE_PASSWORD = "KAFKA_KEYSTORE_PASSWORD";
	
	/**
	 * KAFKAキーストアタイプ取得キー
	 */
	public static final String KAFKA_KEYSTORE_TYPE = "KAFKA_KEYSTORE_TYPE";
	
	/**
	 * KAFKAトラストストアロケーション取得キー
	 */
	public static final String KAFKA_TRUSTSTORE_LOCATION = "KAFKA_TRUSTSTORE_LOCATION";
	
	/**
	 * KAFKAトラストストアパスワード取得キー
	 */
	public static final String KAFKA_TRUSTSTORE_PASSWORD = "KAFKA_TRUSTSTORE_PASSWORD";

	/**
	 * KAFKAトラストストアタイプ取得キー
	 */
	public static final String KAFKA_TRUSTSTORE_TYPE = "KAFKA_TRUSTSTORE_TYPE";
	
	/**
	 * ファイル受信イベントのトピック名
	 */
	public static final String KAFKA_FILE_EVENT_TOPIC = "FileEventTopic";
	/**
	 * 帳票出力用トピック名
	 */
	public static final String KAFKA_REPORT_TOPIC = "PRT_QUEUE";
	/**
	 * 帳票出力用コンシューマカウント数
	 */
	public static final int KAFKA_REPORT_CONSUMER_COUNT = 1;
	/**
	 * 帳票出力用コンシューマグループ名
	 */
	public static final String KAFKA_REPORT_TOPIC_GROUP = "ReportOutputGroup";
	/**
	 * ファイル受信イベントのコンシューマグループ名
	 */
	public static final String KAFKA_FILE_EVENT_GROUP = "FileEventGroup";
	
	/**
	 * ファイルイベントのコンシューマ接続数取得キー
	 */
	public static final String KAFKA_FILE_CONSUMER_COUNT = "KAFKA_FILE_CONSUMER_COUNT";
	
	/**
	 * ファイルイベントのデフォルトコンシューマ接続数
	 */
	public static final int KAFKA_DEFAULT_FILE_CONSUMER_COUNT = 6;
	
	/**
	 * デフォルトのイベントのコンシューマ接続数取得キー
	 */
	public static final String KAFKA_DEFAULT_CONSUMER_COUNT = "KAFKA_DEFAULT_CONSUMER_COUNT";
	
	/**
	 * デフォルトのイベントのコンシューマ接続数(未定義時のプログラム設定値)
	 */
	public static final int KAFKA_DEFAULT_PROG_CONSUMER_COUNT = 6;
	
	/**
	 * KAFKAアプリケーションアダプタールートIDのプレフィックス
	 */
	public static final String KAFKA_APPLICATION_ROUTE_PREFIX = "KAD";
	
	/**
	 * KAFKAコンシューマルートIDのプレフィックス
	 */
	public static final String KAFKA_CONSUMER_PREFIX = "KCM";
	
	/**
	 * コンシューマグループプレフィックス名
	 */
	public static final String KAFKA_CONSUMER_GROUP_PREFIX = "KAFKA_CONSUMER_GROUP_PREFIX";
	
	/**
	 * コンシューマグループプレフィックス名
	 */
	public static final String KAFKA_DEFAULT_CONSUMER_GROUP_PREFIX = "";
	
	/**
	 * KAFKAのAcks数指定取得キー
	 */
	public static final String KAFKA_REQUIRED_ACKS = "KAFKA_REQUIRED_ACKS";
	
	/**
	 * デフォルトのAcks
	 */
	public static final String KAFKA_DEFAULT_REQUIRED_ACKS = "2";
	
	/**
	 * KAFKAリカバリーコマンドのコンシューマグループ
	 */
	public static final String KAFKA_RECOVERY_COMMAND_CONSUMER_GROUP = "REST_COMMAND_CONSUMER_";
	
	/**
	 * MQメッセージリカバリー用保存トピックのプレフィックス
	 */
	public static final String KAFKA_SAVE_TOPIC_PREFIX = "SAVE_";
	
	/**
	 * MQメッセージリカバリー用保存トピックのプレフィックス
	 */
	public static final String KAFKA_HEADER_MQ_MESSAGE_ID = "mq_messageId";
	
	/**
	 * KAFKA機能のモック指定キー
	 */
	public static final String KAFKA_MOCK = "KAFKA_MOCK";
}
