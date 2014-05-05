package net.ipetty.core.web.rest.exception;

/**
 * REST层公用的Exception.
 * 
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 */
public class RestException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 6823199809661975567L;

	public RestException() {
		super();
	}

	public RestException(String message) {
		super(message);
	}

	public RestException(Throwable cause) {
		super(cause);
	}

	public RestException(String message, Throwable cause) {
		super(message, cause);
	}

}
