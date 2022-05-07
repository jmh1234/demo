package com.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * CacheKey
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
@Setter
@Getter
public class CacheKey {
    private String methodName;
    private Object thisObject;
    private Object[] arguments;

    public CacheKey(String methodName, Object thisObject, Object[] arguments) {
        this.methodName = methodName;
        this.thisObject = thisObject;
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CacheKey cacheKey = (CacheKey) o;
        if (!Objects.equals(methodName, cacheKey.methodName)) {
            return false;
        }
        if (!Objects.equals(thisObject, cacheKey.thisObject)) {
            return false;
        }
        return Arrays.equals(arguments, cacheKey.arguments);
    }

    @Override
    public int hashCode() {
        int result = methodName != null ? methodName.hashCode() : 0;
        result = 31 * result + (thisObject != null ? thisObject.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }
}
