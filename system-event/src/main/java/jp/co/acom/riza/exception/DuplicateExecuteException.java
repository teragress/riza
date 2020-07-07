package jp.co.acom.riza.exception;

public class DuplicateExecuteException extends RuntimeException {

	public DuplicateExecuteException(Exception ex) {
		super(ex);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
