package com.test.demo.example.Ioc;

import com.test.demo.controller.TestBootController;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class MyIoCContainer {
    private Properties properties = new Properties();
    private static Map<String, Object> beansMap = new HashMap<>();

    // 实现一个简单的IoC容器，使得：
    // 1. 从beans.properties里加载bean定义
    // 2. 自动扫描bean中的@Autowired注解并完成依赖注入
    public static void main(String[] args) {
        MyIoCContainer container = new MyIoCContainer();
        container.start();
        TestBootController userController = (TestBootController) container.getBean("TestBootController");
        userController.addUserInfo();
    }

    public MyIoCContainer() {
        try {
            properties.load(MyIoCContainer.class.getResourceAsStream("/ioc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 启动该容器
    public void start() {
        properties.forEach(MyIoCContainer::addInstance2BeansMap);
        beansMap.forEach((beanName, beanInstance) -> dependencyInject(beanInstance, beansMap));
    }

    private static void addInstance2BeansMap(Object beanName, Object beanClass) {
        try {
            Class<?> aClass = Class.forName((String) beanClass);
            Object beanInstance = aClass.getConstructor().newInstance();
            beansMap.put((String) beanName, beanInstance);
        } catch (ClassNotFoundException | InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void dependencyInject(Object beanInstance, Map<String, Object> beansMap) {
        List<Field> dependencyFields = Arrays.stream(beanInstance.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(Resource.class) != null)
                .collect(Collectors.toList());
        dependencyFields.forEach(field -> addDependencyOfBean(field, beanInstance, beansMap));
    }

    private void addDependencyOfBean(Field field, Object beanInstance, Map<String, Object> beansMap) {
        try {
            field.setAccessible(true);
            Object dependencyOfBean = beansMap.get(field.getName());
            field.set(beanInstance, dependencyOfBean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // 从容器中获取一个bean
    public Object getBean(String beanName) {
        return beansMap.get(beanName);
    }
}
