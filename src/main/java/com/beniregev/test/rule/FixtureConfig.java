package com.beniregev.test.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leon.kay
 * Utility interface to be used for Configuring Junit Rules for individual tests.
 * Each mocking method referenced in the rule should return a List of mocks or the mock that we want to verify
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface FixtureConfig {
    /**
     * A string description of the config to be used by the FixtureRule to do something with.
     * Values referenced should be method names in the target test class
     * @return
     */
    String[] value() default "";
}
