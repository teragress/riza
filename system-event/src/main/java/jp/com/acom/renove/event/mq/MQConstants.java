package jp.com.acom.renove.event.mq;

/**
 * MQ関連のコンスタント定義
 *
 * @author developer
 *
 */
public class MQConstants {

	/**
	 * 接続維持タイムアウト(㍉秒指定)
	 */
	public static final String MQ_CONNECTION_KEEP_TIMEOUT = "MQ_CONNECTION_KEEP_TIMEOUT";
	/**
	 * 接続維持タイムアウトデフォルト値(一時間<㍉秒指定>)
	 */
	public static final int MQ_DEFAULT_CONNECTION_KEEP_TIMEOUT = 3600000;

	/**
	 * 最大接続数
	 */
	public static final String MQ_MAX_CONNECTION = "MQ_MAX_CONNECTION";
	/**
	 * 最大接続数デフォルト値
	 */
	public static final int MQ_DEFAULT_MAX_CONNECTION = 75;
	/**
	 * 最大未使用接続数
	 */
	public static final String MQ_MAX_UNUSE_CONNECTION = "MQ_MAX_UNUSE_CONNECTION";
	/**
	 * 最大未使用接続数デフォルト値
	 */
	public static final int MQ_DEFAULT_MAX_UNUSE_CONNECTION = 50;

	/**
	 * 接続先キューマネージャー名
	 */
	public static final String MQ_CONNECTION_QMGR = "MQ_CONNECTION_QMGR";
	/**
	* 接続先ホスト
	*/
	public static final String MQ_CONNECTION_HOST = "MQ_CONNECTION_HOST";
	/**
	 * 接続先ポート番号
	 */
	public static final String MQ_CONNECTION_PORT = "MQ_CONNECTION_PORT";
	/**
	* 接続先チャネル名
	*/
	public static final String MQ_CONNECTION_CHANNEL = "MQ_CONNECTION_CHANNEL";
	/**
	* 接続先ユーザー
	*/
	public static final String MQ_CONNECTION_USER = "MQ_CONNECTION_USER";


}
