package com.demo.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * 自定义Log注解
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
}
