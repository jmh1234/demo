package com.demo.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * 自定义Autowired注解
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
