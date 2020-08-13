package com.demo.example.annotation;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Scope("request")
public @interface MyAnnotations {
    MyAnnotation[] value();
}
