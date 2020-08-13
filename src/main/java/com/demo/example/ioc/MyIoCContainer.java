package com.demo.example.ioc;

import com.demo.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class MyIoCContainer {
    private static Map<String, Object> beansMap = new HashMap<>();

    public static void main(String[] args) {
        MyIoCContainer container = new MyIoCContainer();
        container.start();
        OrderService orderService = (OrderService) container.getBean("orderService");
        orderService.createOrder();
    }

    // 启动该容器
    public void start() {
        try {
            Properties properties = new Properties();
            properties.load(MyIoCContainer.class.getResourceAsStream("/ioc.properties"));
            properties.forEach(MyIoCContainer::addInstance2BeansMap);
            beansMap.forEach((beanName, beanInstance) -> dependencyInject(beanInstance, beansMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                .filter(field -> field.getAnnotation(Autowired.class) != null)
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
