package jp.co.acom.riza.event.config;

/**
 * イベント用のメッセージID定義
 *
 * @author teratani
 *
 */
public interface EventMessageId {
	public static final String EVENT_COMMAND_START = "RIZAE001";
	public static final String EVENT_COMMAND_END = "RIZAE002";
	public static final String KAFKA_MESSAGE_NOT_FOUND = "RIZAE003";
	public static final String CONSUMER_ROUTE_EXCEPTION = "RIZAE004";
	public static final String EXCEPTION_INFORMATION = "RIZAE005";
	public static final String KAFKA_MESSAGE_RECOVERY = "RIZAE006";
	public static final String COMMAND_EXCEPTION = "RIZAE007";
	public static final String EVENT_EXCEPTION = "RIZAE008";
	public static final String MQPUT_EXCEPTION = "RIZAE009";
	public static final String MQPUT_RETRY_OVER = "RIZAE010";
	public static final String WARNING_EXCEPTION_INFORMATION = "RIZAE011";
	public static final String RECORD_NOT_FOUND = "RIZAE012";
	public static final String SAVE_KAFKA_MESSAGE_NOT_FOUND = "RIZAE013";
	public static final String EVENT_RECOVERY_EXECUTE = "RIZAE014";
	public static final String CONSUMER_ROUTE_DUPLICATION = "RIZAE015";
	public static final String CONSUMER_THREAD_POOL = "RIZAE016";
	public static final String CHECKPOINT_CLEANUP = "RIZAE017";
	public static final String TRANEXEC_CLEANUP = "RIZAE018";
	public static final String CEP_ERROR = "RIZAE019";
	

}
