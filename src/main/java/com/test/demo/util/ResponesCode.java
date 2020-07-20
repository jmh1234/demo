package com.test.demo.util;

import java.time.LocalDateTime;
import java.util.Map;

public class ResponesCode {

    private int code;

    private LocalDateTime resTime;

    private String msg;

    private Boolean status;

    private Map<String,Boolean> authorities;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LocalDateTime getResTime() {
        return resTime;
    }

    public void setResTime(LocalDateTime resTime) {
        this.resTime = resTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Map<String,Boolean> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Map<String,Boolean> authorities) {
        this.authorities = authorities;
    }
}
