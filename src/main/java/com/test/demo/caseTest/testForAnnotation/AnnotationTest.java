package com.test.demo.caseTest.testForAnnotation;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Scope("request")
@Repeatable(com.test.demo.caseTest.testForAnnotation.AnnotationTests.class)

public @interface AnnotationTest {

    String id();

    String description() default "no description";
}
