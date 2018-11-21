package org.esb.server.socket;

public class SocketException extends RuntimeException{

	private static final long serialVersionUID = -5398470699374066651L;

	public SocketException() {
		super();
	}

	public SocketException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SocketException(String message, Throwable cause) {
		super(message, cause);
	}

	public SocketException(String message) {
		super(message);
	}

	public SocketException(Throwable cause) {
		super(cause);
	}
	

}
