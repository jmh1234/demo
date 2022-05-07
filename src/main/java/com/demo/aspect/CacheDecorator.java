package com.demo.aspect;

import com.demo.annotation.Cache;
import com.demo.entity.CacheKey;
import com.demo.entity.CacheValue;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * 缓存装饰器
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
public class CacheDecorator {

    private CacheDecorator() {

    }

    public static <T> T getInstance(Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return decorate(clazz).getConstructor().newInstance();
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> decorate(Class<T> clazz) {
        return (Class<T>) new ByteBuddy().subclass(clazz)
                .method(ElementMatchers.isAnnotatedWith(Cache.class))
                .intercept(MethodDelegation.to(CacheInterceptor.class))
                .make()
                .load(clazz.getClassLoader())
                .getLoaded();
    }

    /**
     * 内部类
     * 缓存拦截器
     */
    public static class CacheInterceptor {

        private CacheInterceptor() {

        }

        private static final ConcurrentHashMap<CacheKey, CacheValue> CONCURRENT_HASHMAP = new ConcurrentHashMap<>();

        @RuntimeType
        public static Object cache(
                @SuperCall Callable<List<Object>> superMethod,
                @Origin Method method,
                @This Object thisObject,
                @AllArguments Object[] arguments) throws Exception {
            CacheKey cacheKey = new CacheKey(method.getName(), thisObject, arguments);
            CacheValue cacheValue = CONCURRENT_HASHMAP.get(cacheKey);
            if (cacheValue != null) {
                if (isMatchesCacheExistsTime(cacheValue, method)) {
                    return getRealMethodResult(superMethod, cacheKey);
                } else {
                    return cacheValue.getValue();
                }
            } else {
                return getRealMethodResult(superMethod, cacheKey);
            }
        }

        private static boolean isMatchesCacheExistsTime(CacheValue cacheValue, Method method) {
            long time = cacheValue.getTime();
            return System.currentTimeMillis() - time > method.getAnnotation(Cache.class).cacheSeconds() * 1000;
        }

        private static List<Object> getRealMethodResult(Callable<List<Object>> superMethod, CacheKey cacheKey) throws Exception {
            List<Object> realMethodResultList = superMethod.call();
            CONCURRENT_HASHMAP.put(cacheKey, new CacheValue(realMethodResultList, System.currentTimeMillis()));
            return realMethodResultList;
        }
    }
}
