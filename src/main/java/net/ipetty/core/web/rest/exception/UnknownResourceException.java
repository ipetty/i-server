package net.ipetty.core.web.rest.exception;

/**
 * Simulated business-logic exception indicating a desired business entity or
 * record cannot be found.
 */
public class UnknownResourceException extends RestException {

	/** serialVersionUID */
	private static final long serialVersionUID = 3656386735762851035L;

	public UnknownResourceException() {
		super();
	}

	public UnknownResourceException(String msg) {
		super(msg);
	}

	public UnknownResourceException(Throwable cause) {
		super(cause);
	}

	public UnknownResourceException(String message, Throwable cause) {
		super(message, cause);
	}

}
