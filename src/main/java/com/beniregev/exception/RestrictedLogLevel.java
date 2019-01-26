package com.beniregev.exception;

import org.slf4j.spi.LocationAwareLogger;

import java.lang.annotation.*;

@Inherited
@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RestrictedLogLevel {
    /**
     * The desired log level used to log the annotated exception.
     */
    LogLevel level(); //default LogLevel.ERROR;

    /**
     * Indicates whether to log the stack trace.
     */
    boolean logStackTrace(); //default true;

    /**
     * Enumeration for log levels.
     * @author Ameer Antar
     */
    public enum LogLevel {
        /**
         * FATAL.
         */
        FATAL(LocationAwareLogger.ERROR_INT),
        /**
         * ERROR.
         */
        ERROR(LocationAwareLogger.ERROR_INT),
        /**
         * WARN.
         */
        WARN(LocationAwareLogger.WARN_INT),
        /**
         * INFO.
         */
        INFO(LocationAwareLogger.INFO_INT),
        /**
         * DEBUG.
         */
        DEBUG(LocationAwareLogger.DEBUG_INT),
        /**
         * TRACE.
         */
        TRACE(LocationAwareLogger.TRACE_INT),
        /**
         * OFF.
         */
        OFF(-1);

        private int intLevel;

        private LogLevel(final int intLevel) {
            this.intLevel = intLevel;
        }

        /**
         * Returns the integer value corresponding to the appropriate log level according to SLF4J.
         * @return the integer value corresponding to the appropriate log level according to SLF4J
         */
        public int toIntLevel() {
            return intLevel;
        }

    }
}
