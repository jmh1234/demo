package com.demo.example.annotation;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * 我的自定义注解
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
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
