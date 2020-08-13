package com.test.demo;

import com.test.demo.example.annotation.AnnotationTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnnotationTestMain {

    @Test
    @AnnotationTest(id = "47")
    @AnnotationTest(id = "48", description = "Passwords must contain at least one numeric")
    public void test() {
        List<Integer> caseGather = new ArrayList<>();
        Collections.addAll(caseGather, 47, 48, 49, 50);
        try {
            Class<?> clazz = Class.forName("com.test.demo.annotation.AnnotationTestMain");
            // 获取所有方法的注入值
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                AnnotationTest[] annotations = method.getDeclaredAnnotationsByType(AnnotationTest.class);
                for (AnnotationTest annotation : annotations) {
                    if (annotation != null) {
                        System.out.println("Found Use Case: " + annotation.id() + " --> " + annotation.description());
                        caseGather.remove(new Integer(annotation.id()));
                    }
                }
            }

            /*// 单独获取某一函数的注释值
            Method method = clazz.getDeclaredMethod("trackUseAnn");
            AnnotationTest[] annotations = method.getDeclaredAnnotationsByType(AnnotationTest.class);
            for (AnnotationTest annotation : annotations) {
                if (annotation != null) {
                    System.out.println("Found Use Case: " + annotation.id() + " --> " + annotation.description());
                    caseGather.remove(new Integer(annotation.id()));
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i : caseGather) {
            System.out.println("Warning: Missing use case - " + i);
        }
    }
}
