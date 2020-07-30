package jp.co.acom.riza.event.config;

/**
 * イベント用のコンスタント定義
 *
 * @author teratani
 *
 */
public interface EventConstants {
	/**
	 *　起動モード(ONLINE/BATCH)
	 */
	public static final String EVENT_START_MODE = "EVENT_START_MODE";
	
	/**
	 *　デフォルト起動モード
	 */
	public static final String EVENT_DEFAULT_START_MODE = "ONLINE";
	
	/**
	 *　スレッドプールサイズプロパティ定義取得キー
	 */
	public static final String EVENT_THREAD_POOL_SIZE = "EVENT_THREAD_POOL_SIZE";
	
	/**
	 *　スレッドプール最大サイズプロパティ定義取得キー
	 */
	public static final String EVENT_THREAD_MAX_POOL_SIZE = "EVENT_THREAD_MAX_POOL_SIZE";
	
	/**
	 *　デフォルトスレッドプールサイズ
	 */
	public static final int EVENT_DEFAULT_THREAD_POOL_SIZE = 50;
	
	/**
	 *　デフォルト最大スレッドプールサイズー
	 */
	public static final int EVENT_DEFAULT_THREAD_MAX_POOL_SIZE = 50;
	
	/**
	 *　スレッドプールキューサイズプロパティ定義取得キー
	 */
	public static final String EVENT_THREAD_MAX_QUE_SIZE = "EVENT_THREAD_MAX_QUE_SIZE";
	
	/**
	 *　スレッドプール最大キューサイズプロパティ定義取得キー
	 */
	public static final int EVENT_DEFAULT_THREAD_MAX_QUE_SIZE = 5000;
	
	/**
	 *　スレッドプールID
	 */
	public static final String EVENT_THREAD_POOL_ID = "ThreadPoolProfile";
	
	/**
	 *　スレッドプールBean名
	 */
	public static final String EVENT_THREAD_POOL_BEAN = "ThreadPoolBean";
	
	/**
	 *　エンティティイベントを伝搬するexchangeのヘッダーキー
	 */
	public static final String EXCHANGE_HEADER_ENTITY_EVENT = "EntityEvent";
	
	/**
	 * チェックポイントテーブルメッセージの分割サイズキー
	 * 
	 */
	public static final String CHECK_POINT_SPLIT_SIZE = "CHECK_POINT_SPLIT_SIZE";
	
	/**
	 * デフオルトのチェックポイントテーブルメッセージの分割サイズ 
	 */
	public static final Integer DEFAULT_CHECK_POINT_SPLIT_SIZE = 240000;
}
