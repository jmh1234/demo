package com.demo;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyAnnotation {

    @Test
    @com.demo.example.annotation.MyAnnotation(id = "47")
    @com.demo.example.annotation.MyAnnotation(id = "48", description = "Passwords must contain at least one numeric")
    public void test() {
        List<Integer> caseGather = new ArrayList<>();
        Collections.addAll(caseGather, 47, 48, 49, 50);
        try {
            // 获取所有方法的注入值
            Method[] methods = this.getClass().getMethods();
            for (Method method : methods) {
                com.demo.example.annotation.MyAnnotation[] myAnnotations = method.getDeclaredAnnotationsByType(com.demo.example.annotation.MyAnnotation.class);
                for (com.demo.example.annotation.MyAnnotation myAnnotation : myAnnotations) {
                    if (myAnnotation != null) {
                        System.out.println("Found Use Case: " + myAnnotation.id() + " --> " + myAnnotation.description());
                        caseGather.remove(new Integer(myAnnotation.id()));
                    }
                }
            }

            // 单独获取某一函数的注释值
            /*Method method = this.getClass().getDeclaredMethod("trackUseAnn");
            MyAnnotation[] annotations = method.getDeclaredAnnotationsByType(MyAnnotation.class);
            for (MyAnnotation annotation : annotations) {
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
