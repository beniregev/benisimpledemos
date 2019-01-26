package com.beniregev.test.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author AMERICANWELL\leon.kay
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface TestFixture {

    /**
     * The Name of member variable to Store in the FixtureRule.
     * be referenced in {@link @FixtureConfig} for invoking Mocking Functions with Parameters.
     * Note, member variables annotated with this, do Not Have to be mocks.
     * @return -
     */
    String value();
}
