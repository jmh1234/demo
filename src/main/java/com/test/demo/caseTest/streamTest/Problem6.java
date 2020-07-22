package com.test.demo.caseTest.streamTest;

import java.util.Set;
import java.util.stream.Collectors;

public class Problem6 {
    // 使用流的方法，把所有长度等于1的单词挑出来，然后用逗号连接起来
    // 例如，传入参数words=['a','bb','ccc','d','e']  返回字符串a,d,e
    public static String filterThenConcat(Set<String> words) {
        return words.stream().filter(s -> s.length() == 1).map(Object::toString).collect(Collectors.joining(","));
    }
}
