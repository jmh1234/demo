package com.demo.example.ioc;

/**
 * Created with IntelliJ IDEA.
 * User
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class User {
    private final Integer id;
    private final String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
