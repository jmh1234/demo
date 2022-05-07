package com.demo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 * CacheValue
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
@Getter
@Setter
public class CacheValue {
    private Object value;
    private long time;

    public CacheValue(Object value, long time) {
        this.value = value;
        this.time = time;
    }
}
