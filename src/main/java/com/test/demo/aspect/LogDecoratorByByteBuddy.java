package com.test.demo.aspect;

import com.test.demo.Test;
import com.test.demo.annotation.Log;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.concurrent.Callable;

public class LogDecoratorByByteBuddy {
    public static <T> T getInstance(Class<T> clazz) throws Exception {
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

    @SuppressWarnings("unused")
    public static class LoggerInterceptor {
        public static String handle(@SuperCall Callable<String> superMethod, @AllArguments Object[] args) throws Exception {
            System.out.println("this is running by byteBuddy and params is " + args[0]);
            String invoke = superMethod.call();
            System.out.println("this is running by byteBuddy and result is " + invoke);
            return invoke;
        }
    }
}
