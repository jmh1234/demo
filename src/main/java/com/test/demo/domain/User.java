package com.test.demo.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class User {
    private String id;
    private String tel;
    private String name;
    private String address;
    private Timestamp createTime;
    private Timestamp updateTime;
    private int status;
    private String username;
    private String password;
    private int age;

    public User(String id, String tel, String address) {
        this.id = id;
        this.tel = tel;
        this.address = address;
    }

    public User() {

    }
}
