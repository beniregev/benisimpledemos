package com.beniregev.test.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Binyamin Regev
 * An annotation that can be placed on the mocking methods referenced
 * by the annotation FixtureConfig on the unit test. Can be used to provide
 * insight into what the mocking method is doing for reporting purposes.
 * Leveraged by {@see DocRule}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface MockDoc {
    /**
     * A description of the mocking that is being performed
     * @return
     */
    String value();
}
