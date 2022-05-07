package com.demo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 * 用户后台向前台返回的JSON对象
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
@Setter
@Getter
public class ResponseResult {
    private String resCode;
    private String resMsg;

    @Override
    public String toString() {
        return "ResponseResult {resCode=" + resCode + ", resMsg=" + resMsg + "}";
    }
}
