package com.demo.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * 通知界面注解
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AdviceAspect {
    String description();
}
