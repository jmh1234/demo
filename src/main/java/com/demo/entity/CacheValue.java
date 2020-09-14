package com.demo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacheValue {
    public Object value;
    public long time;

    public CacheValue(Object value, long time) {
        this.value = value;
        this.time = time;
    }
}
