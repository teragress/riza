package jp.co.acom.riza.event.exception;

/**
 * イベントコマンド例外
 * @author teratani
 *
 */
public class EventCommandException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EventCommandException() {
		super();
	}

	public EventCommandException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EventCommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventCommandException(String message) {
		super(message);
	}

	public EventCommandException(Throwable cause) {
		super(cause);
	}
}
