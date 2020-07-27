package jp.co.acom.riza.exception;

/**
 * 重複実行例外
 * @author teratani
 *
 */
public class DuplicateExecuteException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public DuplicateExecuteException() {
		super();
	}

	public DuplicateExecuteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DuplicateExecuteException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateExecuteException(String message) {
		super(message);
	}

	public DuplicateExecuteException(Throwable cause) {
		super(cause);
	}

	public DuplicateExecuteException(Exception ex) {
		super(ex);
	}

}
