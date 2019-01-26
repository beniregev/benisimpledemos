package com.beniregev.test.rule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.easymock.MockType;

/**
 * @author leon.kay
 * Annotation to tell fixture rule how to replace the Singleton instance on a class with an EasyMock
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface MockSingleton {
    String field() default "instance";
    MockType type() default MockType.DEFAULT;
}
