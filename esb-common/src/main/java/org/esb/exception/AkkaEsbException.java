package org.esb.exception;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class AkkaEsbException extends RuntimeException {

    private static final long serialVersionUID = 7394693990868790861L;

    public AkkaEsbException() {
        super();
    }

    public AkkaEsbException(String message, Throwable cause) {
        super(message, cause);
    }

    public AkkaEsbException(String message) {
        super(message);
    }

    public AkkaEsbException(Throwable cause) {
        super(cause);
    }


}
