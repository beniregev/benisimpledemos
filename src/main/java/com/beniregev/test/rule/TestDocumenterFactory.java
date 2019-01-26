package com.beniregev.test.rule;

import com.beniregev.util.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility for generating Test Documenters
 * @author AMERICANWELL\leon.kay
 *
 */
public class TestDocumenterFactory {

    private static final Log LOG = LogFactory.getLog(TestDocumenterFactory.class);

    /**
     * Generate the Default TestDocumenter. In this case, its a LogDocumenter,
     * but this can change in the future.
     * @return a TestDocumenter implementation.
     */
    public static TestDocumenter getDefault() {
        return getDefault(null);
    }

    public static TestDocumenter getDefaultByName(final String className) {
        return new LogDocumenter(LogFactory.getLog(className));
    }

    /**
     * Generate the Default TestDocumenter. In this case, its a LogDocumenter,
     * but this can change in the future.
     * @param className - The test class to generate a documenter for.
     * @return a TestDocumenter implementation.
     */
    public static TestDocumenter getDefault(final Class<?> className) {
        if (className == null) {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            String classNameString = trace[3].getClassName();
            Logger.info(LOG, "No Class Name Specified, defaulting to {0}", classNameString);
            return new LogDocumenter(LogFactory.getLog(classNameString));
        }
        return new LogDocumenter(LogFactory.getLog(className));
    }
}
