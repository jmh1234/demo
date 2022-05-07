package com.demo.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * 自定义Cache注解
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    // 标记缓存的时长（秒），默认60s
    int cacheSeconds() default 60;
}
