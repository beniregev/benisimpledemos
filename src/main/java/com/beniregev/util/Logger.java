package com.beniregev.util;

import com.beniregev.exception.RestrictedLogLevel;
import com.beniregev.exception.RestrictedLogLevel.LogLevel;
import org.apache.commons.logging.Log;
import org.apache.log4j.Level;
import org.slf4j.spi.LocationAwareLogger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.text.DecimalFormat;
import java.text.MessageFormat;

public final class Logger {
    private static final int EXCEPTION_BUFFER_SIZE = 512;
    private static final String FQCN = Logger.class.getName();
    private static final boolean isDevMode = Boolean.TRUE;

    private Logger() {
        // static only
    }

    /**
     * Log the message, with parameter placeholders resolved, if debug is enabled.
     * @param logger the logger
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    public static void debug(final Log logger, final Object message, final Object... parameters) {
        if (logger.isDebugEnabled()) {
            log(logger, RestrictedLogLevel.LogLevel.DEBUG, message, parameters);
        }
    }

    /**
     * Log the message, with parameter placeholders resolved, if debug is enabled.
     * @param logger the logger
     * @param t a throwable object
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    public static void debug(final Log logger, final Throwable t, final Object message, final Object... parameters) {
        if (logger.isDebugEnabled()) {
            log(logger, LogLevel.DEBUG, t, message, parameters);
        }
    }

    /**
     * Log the message, with parameter placeholders resolved, if error is enabled.
     * @param logger the logger
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    public static void error(final Log logger, final Object message, final Object... parameters) {
        if (logger.isErrorEnabled()) {
            log(logger, LogLevel.ERROR, message, parameters);
        }
    }

    /**
     * Log the message, with parameter placeholders resolved, if error is enabled.
     * @param logger the logger
     * @param t a throwable object
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    public static void error(final Log logger, final Throwable t, final Object message, final Object... parameters) {
        if (logger.isErrorEnabled()) {
            log(logger, LogLevel.ERROR, t, message, parameters);
        }
    }

    /**
     * Log the message, with parameter placeholders resolved, if info is enabled.
     * @param logger the logger
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    public static void info(final Log logger, final Object message, final Object... parameters) {
        if (logger.isInfoEnabled()) {
            log(logger, LogLevel.INFO, message, parameters);
        }
    }

    /**
     * Log the message, with parameter placeholders resolved, if info is enabled.
     * @param logger the logger
     * @param t a throwable object
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    public static void info(final Log logger, final Throwable t, final Object message, final Object... parameters) {
        if (logger.isInfoEnabled()) {
            log(logger, LogLevel.INFO, t, message, parameters);
        }
    }

    /**
     * Log the message, with parameter placeholders resolved, if trace is enabled.
     * @param logger the logger
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    public static void trace(final Log logger, final Object message, final Object... parameters) {
        if (logger.isTraceEnabled()) {
            log(logger, LogLevel.TRACE, message, parameters);
        }
    }

    /**
     * Log the message, with parameter placeholders resolved, if trace is enabled.
     * @param logger the logger
     * @param t a throwable object
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    public static void trace(final Log logger, final Throwable t, final Object message, final Object... parameters) {
        if (logger.isTraceEnabled()) {
            log(logger, LogLevel.TRACE, t, message, parameters);
        }
    }

    /**
     * Log the message, with parameter placeholders resolved, if warn is enabled.
     * @param logger the logger
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    public static void warn(final Log logger, final Object message, final Object... parameters) {
        if (logger.isWarnEnabled()) {
            log(logger, LogLevel.WARN, message, parameters);
        }
    }

    /**
     * Log the message, with parameter placeholders resolved, if warn is enabled.
     * @param logger the logger
     * @param t a throwable object
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    public static void warn(final Log logger, final Throwable t, final Object message, final Object... parameters) {
        if (logger.isWarnEnabled()) {
            log(logger, LogLevel.WARN, t, message, parameters);
        }
    }

    /**
     * Access the Log4J logger directly so we can override the FQCN to make sure if we've turned on class and/or method
     * logging that the calling class is logged, not this one.
     * @param logger the logger
     * @param level the log level
     * @param message the message, possibly with parameter placeholders
     * @param parameters optional parameters for placeholder replacement
     */
    static void log(final Log logger, final LogLevel level, final Object message, final Object... parameters) {
        log(logger, level, null, message, parameters);
    }

    /**
     * Format and log message text. The supplied message pattern should conform to the syntax used by
     * {@link MessageFormat} and the resulting text will be rendered according to the rules of
     * {@link MessageFormat} with the exception that integer and long parameters are rendered without
     * thousands separators.
     * @param logger the logger The logger.
     * @param level the log level The log level.
     * @param t a throwable object Optional throwable to be logged.
     * @param message The message, possibly with parameter placeholders following the rules of {@link MessageFormat}.
     * @param parameters Optional parameters for placeholder replacement.
     */
    static void log(final Log logger, final LogLevel level, final Throwable t, final Object message,
                    final Object... parameters) {

        final String formatted;
        if (parameters == null || parameters.length == 0) {
            formatted = String.valueOf(message);
        }
        else {
            final MessageFormat formatter = new MessageFormat(message.toString());
            // Explicitly set format for integers and longs to suppress separators.
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] instanceof Long || parameters[i] instanceof Integer) {
                    formatter.setFormatByArgumentIndex(i, new DecimalFormat("#"));
                }
            }
            formatted = formatter.format(parameters);
        }
        log(logger, level, formatted, t);
    }

    /**
     * Logs the exception to the appropriate log level if the exception is annotated with
     * <code>@RestrictedLogLevel</code>. Otherwise log normally.
     * @param log the logger
     * @param logLevel the desired log level
     * @param message the error message
     * @param t the exception
     */
    private static void log(final Log log, final LogLevel logLevel, final Object message, final Throwable t) {
        final RestrictedLogLevel classRll = getRestrictedLogLevelAnnotation(t);

        if (classRll == null) {
            final RestrictedLogLevel rll = new RestrictedLogLevel() {
                @Override
                public LogLevel level() {
                    return logLevel;
                }
                @Override
                public boolean logStackTrace() {
                    return true;
                }
                @Override
                public Class<? extends Annotation> annotationType() {
                    return RestrictedLogLevel.class;
                }
            };
            log(log, message, t, rll);
        }
        else {
            log(log, message, t, classRll);
        }
    }

    /**
     * Returns the <code>RestrictedLogLevel</code> annotation if any in the given object class or its hierarchy.
     * @param o the object which will be searched for the annotation
     * @return the <code>RestrictedLogLevel</code> annotation if any in the given object class or its hierarchy;
     *         otherwise null
     */
    private static RestrictedLogLevel getRestrictedLogLevelAnnotation(final Object o) {
        final Class<?> clazz = o == null ? null : o.getClass();
        return clazz != null ? clazz.getAnnotation(RestrictedLogLevel.class) : null;
    }

    /**
     * Log using an appropriate logging mechanism.
     * @param log the log
     * @param message the message
     * @param t a throwable object (may be null)
     * @param rll the <code>RestrictedLogLevel</code> object
     */
    private static void log(final Log log, final Object message, final Throwable t,
                            final RestrictedLogLevel rll) {

        if (isDevMode) {
            logLog4j(log, message, t, rll);
        }
        else {
            logDefault(log, message, t, rll);
        }
    }

    /**
     * Log using the Log4j implementation interface. (Properly removes references of this wrapper class from the log
     * statement).
     * @param log the log
     * @param message the message
     * @param t a throwable object (may be null)
     * @param rll the <code>RestrictedLogLevel</code> object
     */
    private static void logLog4j(final Log log, final Object message, final Throwable t,
                                 final RestrictedLogLevel rll) {
        Object wrappedLogger = ReflectionUtils.getFieldValue(log, "logger");
        if (org.apache.log4j.Logger.class.isInstance(wrappedLogger)) {
            org.apache.log4j.Logger log4jLogger = (org.apache.log4j.Logger) wrappedLogger;
            // we must convert from the Slf4j int value to the Log4j enum value
            Level log4jLevel = forLevel(rll.level());
            log4jLogger.log(FQCN, log4jLevel,
                    String.valueOf(rll.logStackTrace() ? message : buildExceptionMessage(message, t)),
                    rll.logStackTrace() ? t : null);
        } else {
            final LocationAwareLogger logAdapter = (LocationAwareLogger) wrappedLogger;
            logAdapter.log(null, FQCN, rll.level().toIntLevel() /* use Slf4j level value */,
                    String.valueOf(rll.logStackTrace() ? message : buildExceptionMessage(message, t)),
                    (Object[]) null,
                    rll.logStackTrace() ? t : null);
        }
    }

    /**
     * Convert an internal {@link LogLevel} to the appropriate Log4j {@link Level} representation, rather than using the {@link LocationAwareLogger}
     * constants.
     * @param level the log level
     * @return the Log4j level equivalent (one to one for all except FATAL)
     */
    private static Level forLevel(final LogLevel level) {
        switch (level) {
            case OFF:
                return Level.OFF;
            case WARN:
                return Level.WARN;
            case ERROR:
            case FATAL:
                return Level.ERROR;
            case TRACE:
                return Level.TRACE;
            case DEBUG:
                return Level.DEBUG;
            case INFO:
            default:
                return Level.INFO;
        }
    }

    /**
     * Log using the commons-logging interface.
     * @param log the log
     * @param message the message
     * @param t a throwable object (may be null)
     * @param rll the <code>RestrictedLogLevel</code> object
     */
    private static void logDefault(final Log log, final Object message, final Throwable t,
                                   final RestrictedLogLevel rll) {

        switch (rll.level()) {
            case FATAL:
                log.fatal(rll.logStackTrace() ? message : buildExceptionMessage(message, t),
                        rll.logStackTrace() ? t : null);
                break;
            case ERROR:
                log.error(rll.logStackTrace() ? message : buildExceptionMessage(message, t),
                        rll.logStackTrace() ? t : null);
                break;
            case WARN:
                log.warn(rll.logStackTrace() ? message : buildExceptionMessage(message, t),
                        rll.logStackTrace() ? t : null);
                break;
            case INFO:
                log.info(rll.logStackTrace() ? message : buildExceptionMessage(message, t),
                        rll.logStackTrace() ? t : null);
                break;
            case DEBUG:
                log.debug(rll.logStackTrace() ? message : buildExceptionMessage(message, t),
                        rll.logStackTrace() ? t : null);
                break;
            case TRACE:
                log.trace(rll.logStackTrace() ? message : buildExceptionMessage(message, t),
                        rll.logStackTrace() ? t : null);
                break;
            default:
        }
    }

    /**
     * Builds a log message without including the entire stack trace of the given exception.
     * @param message the message object
     * @param t the exception (may be null)
     * @return a String which includes the message object and the exception's class name and message
     */
    private static Object buildExceptionMessage(final Object message, final Throwable t) {
        if (t == null) {
            return message;
        }
        final StringWriter sw = new StringWriter(EXCEPTION_BUFFER_SIZE);
        final PrintWriter pw = new PrintWriter(sw);
        pw.println(message);
        pw.print(t.getClass().getName());
        pw.print(": ");
        pw.print(t.getMessage());
        return sw;
    }
}
