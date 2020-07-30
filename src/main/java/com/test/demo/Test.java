package com.test.demo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(49, 0, 50);
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return "123".indexOf(o1, o2);
            }
        });
        list.forEach(System.out::println);
    }
}
