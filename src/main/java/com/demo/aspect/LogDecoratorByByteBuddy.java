package com.demo.aspect;

import com.demo.Test;
import com.demo.annotation.Log;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * 通过字节码增强实现日志装饰
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
public class LogDecoratorByByteBuddy {

    private LogDecoratorByByteBuddy() {

    }

    public static <T> T getInstance(Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return enhanceByAnnotation(clazz).getConstructor().newInstance();
    }

    private static <T> Class<? extends T> enhanceByAnnotation(Class<T> clazz) {
        return new ByteBuddy().subclass(clazz)
                .method(ElementMatchers.isAnnotatedWith(Log.class))
                .intercept(MethodDelegation.to(LoggerInterceptor.class))
                .make()
                .load(Test.class.getClassLoader())
                .getLoaded();
    }

    /**
     * 内部类
     * 日志拦截器
     */
    public static class LoggerInterceptor {

        private LoggerInterceptor() {

        }

        public static String handle(@SuperCall Callable<String> superMethod, @AllArguments Object[] args) throws Exception {
            System.out.println("this is running by byteBuddy and params is " + args[0]);
            String invoke = superMethod.call();
            System.out.println("this is running by byteBuddy and result is " + invoke);
            return invoke;
        }
    }
}
