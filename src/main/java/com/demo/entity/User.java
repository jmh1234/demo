package com.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
public class User implements Serializable {
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

    public User() {
    }

    public User(String id, String name, String tel, String address) {
        this.id = id;
        this.tel = tel;
        this.name = name;
        this.address = address;
    }
}
