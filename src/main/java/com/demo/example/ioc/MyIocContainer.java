package com.demo.example.ioc;

import com.demo.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * MyIoCContainer
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class MyIocContainer {
    private Properties properties;
    private static final Map<String, Object> BEANS_MAP = new HashMap<>();

    /**
     * 实现一个简单的IoC容器，使得：
     * 1. 从beans.properties里加载bean定义
     * 2. 自动扫描bean中的@Autowired注解并完成依赖注入
     */
    public MyIocContainer() {
        try {
            Properties prop = new Properties();
            prop.load(MyIocContainer.class.getResourceAsStream("/ioc.properties"));
            this.properties = prop;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动该容器
     */
    public void start() {
        properties.forEach(MyIocContainer::addInstance2BeansMap);
        BEANS_MAP.forEach((beanName, beanInstance) -> dependencyInject(beanInstance));
    }

    private static void addInstance2BeansMap(Object beanName, Object beanClass) {
        try {
            Class<?> aClass = Class.forName((String) beanClass);
            Object beanInstance = aClass.getConstructor().newInstance();
            BEANS_MAP.put((String) beanName, beanInstance);
        } catch (ClassNotFoundException | InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void dependencyInject(Object beanInstance) {
        List<Field> dependencyFields = Arrays.stream(beanInstance.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(Autowired.class) != null)
                .collect(Collectors.toList());
        dependencyFields.forEach(field -> addDependencyOfBean(field, beanInstance));
    }

    private void addDependencyOfBean(Field field, Object beanInstance) {
        try {
            field.setAccessible(true);
            Object dependencyOfBean = MyIocContainer.BEANS_MAP.get(field.getName());
            field.set(beanInstance, dependencyOfBean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从容器中获取一个bean
     *
     * @param beanName beanName
     * @return bean
     */
    public Object getBean(String beanName) {
        return BEANS_MAP.get(beanName);
    }

    public static void main(String[] args) {
        MyIocContainer container = new MyIocContainer();
        container.start();
        OrderService orderService = (OrderService) container.getBean("orderService");
        orderService.createOrder();
    }
}
