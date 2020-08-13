package com.test.demo.example.annotation;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Scope("request")
@Repeatable(MyAnnotations.class)

public @interface MyAnnotation {

    String id();

    String description() default "no description";
}
