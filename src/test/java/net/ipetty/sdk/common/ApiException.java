package net.ipetty.sdk.common;

/**
 * Api层公用的Exception.
 * 
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 */
public class ApiException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = -5849351480627910272L;

	public ApiException() {
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

}
