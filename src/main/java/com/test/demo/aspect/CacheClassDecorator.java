package com.test.demo.aspect;

import com.test.demo.annotation.Cache;
import com.test.demo.domain.CacheKey;
import com.test.demo.domain.CacheValue;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class CacheClassDecorator {
    @SuppressWarnings("unchecked")
    public static <T> Class<T> decorate(Class<T> clazz) {
        return (Class<T>) new ByteBuddy().subclass(clazz)
                .method(ElementMatchers.isAnnotatedWith(Cache.class))
                .intercept(MethodDelegation.to(CacheInterceptor.class))
                .make()
                .load(clazz.getClassLoader())
                .getLoaded();
    }

    public static class CacheInterceptor {
        private static ConcurrentHashMap<CacheKey, CacheValue> concurrentHashMap = new ConcurrentHashMap<>();

        @RuntimeType
        @SuppressWarnings("unused")
        public static Object cache(
                @SuperCall Callable<List<Object>> superMethod,
                @Origin Method method,
                @This Object thisObject,
                @AllArguments Object[] arguments) throws Exception {
            CacheKey cacheKey = new CacheKey(method.getName(), thisObject, arguments);
            CacheValue cacheValue = concurrentHashMap.get(cacheKey);
            if (cacheValue != null)
                if (isMatchesCacheExistsTime(cacheValue, method)) return getRealMethodResult(superMethod, cacheKey);
                else return cacheValue.value;
            else return getRealMethodResult(superMethod, cacheKey);
        }

        private static boolean isMatchesCacheExistsTime(CacheValue cacheValue, Method method) {
            long time = cacheValue.time;
            return System.currentTimeMillis() - time > method.getAnnotation(Cache.class).cacheSeconds() * 1000;
        }

        private static List<Object> getRealMethodResult(Callable<List<Object>> superMethod, CacheKey cacheKey) throws Exception {
            List<Object> realMethodResultList = superMethod.call();
            concurrentHashMap.put(cacheKey, new CacheValue(realMethodResultList, System.currentTimeMillis()));
            return realMethodResultList;
        }
    }
}
