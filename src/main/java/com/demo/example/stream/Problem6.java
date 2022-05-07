package com.demo.example.stream;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Problem6
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class Problem6 {

    private Problem6() {

    }

    /**
     * 使用流的方法，把所有长度等于1的单词挑出来，然后用逗号连接起来
     * 例如，传入参数words=['a','bb','ccc','d','e']  返回字符串a,d,e
     *
     * @param words words
     * @return String
     */
    public static String filterThenConcat(Set<String> words) {
        return words.stream().filter(s -> s.length() == 1).map(Object::toString).collect(Collectors.joining(","));
    }
}
