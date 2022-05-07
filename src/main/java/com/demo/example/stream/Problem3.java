package com.demo.example.stream;

/**
 * Created with IntelliJ IDEA.
 * Problem3
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class Problem3 {

    private Problem3() {

    }

    /**
     * 统计一个给定的字符串中，大写英文字母（A,B,C,...,Z）出现的次数。
     * 例如，给定字符串"AaBbCc1234ABC"，返回6，因为该字符串中出现了6次大写英文字母ABCABC
     *
     * @param str 定的字符串
     * @return 大写字母出现的次数
     */
    public static int countUpperCaseLetters(String str) {
        return (int) str.chars().filter(Character::isUpperCase).count();
    }
}
