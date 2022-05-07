package com.demo.example.stream;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Problem2
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class Problem2 {

    private Problem2() {

    }

    /**
     * 判断一段文本中是否包含关键词列表中的文本，如果包含任意一个关键词，返回true，否则返回false
     * 例如，text="catcatcat,boyboyboy", keywords=["boy", "girl"]，返回true
     * 例如，text="I am a boy", keywords=["cat", "dog"]，返回false
     *
     * @param text     文本
     * @param keywords 关键字
     * @return 是否包含关键字
     */
    public static boolean containsKeyword(String text, List<String> keywords) {
        return keywords.stream().map(text::contains).distinct().anyMatch(isContainKey -> isContainKey);
    }
}
