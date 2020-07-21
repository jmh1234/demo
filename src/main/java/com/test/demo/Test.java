package com.test.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Test {

    private static List<User> isWang(List<User> users) {
        return filter(users, user -> user.getName().startsWith("王"));
    }

    private static boolean userWithEvenNum(User user) {
        return user.getId() % 2 == 0;
    }

    private static List<User> filterUserWithEvenNum(List<User> users) {
        return filter(users, Test::userWithEvenNum);
    }

    private static List<User> filter(List<User> users, Predicate<User> predicate) {
        List<User> resultList = new ArrayList<>();
        for (User user : users) {
            if (predicate.test(user)) {
                resultList.add(user);
            }
        }
        return resultList;
    }

    private static String test(User user, Function<User, String> function) {
        return function.apply(user);
    }

    private static String strTest(User user) {
        return user.toString();
    }

    public static void main(String[] args) {
        String str = test(new User(1, "zhangsan"), Test::strTest);
        System.out.println(str);

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User(i, "王" + i);
            users.add(user);
        }

        users.forEach(user -> System.out.println(user.getName()));

        List<User> resultList = Test.filterUserWithEvenNum(users);
        List<User> resultList1 = Test.isWang(users);
        System.out.println(resultList);
        System.out.println(resultList1);
    }
}

class User {
    private int id;
    private String name;

    User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.id + "-" + this.name;
    }
}
