package com.guoyukun.leman.config;

public class NsException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = 4283390130473640414L;

    /**
     *
     */
    public NsException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public NsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public NsException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NsException(Throwable cause) {
        super(cause);
    }

}
