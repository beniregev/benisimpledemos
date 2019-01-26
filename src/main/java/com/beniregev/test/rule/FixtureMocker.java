package com.beniregev.test.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.ui.context.Theme;

/**
 * @author AMERICANWELL\leon.kay
 * FixtureMocker reference Classes that can change Mocking Behavior.
 * Used in conjunction with {@link @FixtureConfig} to maximize reuse of Mocking Code.
 * Objects annotated with this should be Fields declared as static.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface FixtureMocker {

    /**
     * The Name of {@link Theme} {@link FixtureMocker} that will be stored in {@link FixtureRule} and should
     * be referenced in {@link @FixtureConfig}
     * @return -
     */
    String value();
}
