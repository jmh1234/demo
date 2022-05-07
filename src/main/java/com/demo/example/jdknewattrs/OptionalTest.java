package com.demo.example.jdknewattrs;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * OptionalTest
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class OptionalTest {

    public static void main(String[] args) {
        Integer value2 = 10;
        // Optional.ofNullable - 允许传递为 null 参数
        java.util.Optional<Integer> first = java.util.Optional.empty();
        // Optional.of - 如果传递的参数是 null，抛出异常 NullPointerException
        java.util.Optional<Integer> end = java.util.Optional.of(value2);
        System.out.println(sum(first, end));
    }

    private static Integer sum(Optional<Integer> first, Optional<Integer> end) {
        // Optional.isPresent - 判断值是否存在
        System.out.println("第一个参数值存在: " + first.isPresent());
        System.out.println("第二个参数值存在: " + end.isPresent());
        // Optional.orElse - 如果值存在，返回它，否则返回默认值
        Integer value1 = first.orElse(0);
        Integer value2 = end.orElse(0);
        return value1 + value2;
    }
}
