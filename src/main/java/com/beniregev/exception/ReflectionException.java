package com.beniregev.exception;

public class ReflectionException extends RuntimeException {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 7813215491650663710L;

    /**
     * Create new instance.
     * @param message The message.
     * @param cause The cause.
     */
    public ReflectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Create new instance.
     * @param message The message.
     */
    public ReflectionException(final String message) {
        super(message);
    }

    /**
     * Create new instance.
     * @param cause The cause.
     */
    public ReflectionException(final Throwable cause) {
        super(cause);
    }

}
