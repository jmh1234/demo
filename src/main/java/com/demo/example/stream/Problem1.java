package com.demo.example.stream;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Problem1 {
    public static class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    // 编写一个方法，统计"年龄大于等于60的用户中，名字是两个字的用户数量"
    public static int countUsers(List<User> users) {
        return (int) users.stream().filter(user -> user.age >= 60 && user.name.length() == 2).count();
    }

    // 编写一个方法，筛选出年龄大于等于60的用户，然后将他们按照年龄从大到小排序，将他们的名字放在一个LinkedList中返回
    public static LinkedList<String> collectNames(List<User> users) {
        return users.stream()
                .filter(user -> user.age >= 60)
                .sorted((o1, o2) -> o2.age - o1.age)
                .map(user -> user.name)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
