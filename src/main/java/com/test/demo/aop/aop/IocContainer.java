package com.test.demo.aop.aop;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

//简化版的IOC容器示例
public class IocContainer {
    // 简化版的bean定义Map
    private Map<String, Class<?>> beanDefinitionMap = new HashMap<>();

    // bean容器
    private Map<String, Object> beanMap = new HashMap<>();

    // 简化版切面 （应为一个List，支持多个切面） List<Aspect> aspects = new ArrayList<>();
    private Aspect aspect;

    public void addBean(String beanName, Class<?> beanClass) {
        this.beanDefinitionMap.put(beanName, beanClass);
    }

    public Object getBean(String beanName) throws Exception {
        Object bean = beanMap.get(beanName);
        if (bean == null) {
            bean = this.createInstance(beanName); // 通过该Bean的Class对象创建该Bean的实例
            bean = this.proxyEnhance(bean); // 将切面注入该Bean
            this.beanMap.put(beanName, bean);
        }
        return bean;
    }

    /**
     * 通过类的Class对象创建实例
     *
     * @param beanName 手动添加Bean的名称
     * @return 该Bean的实例
     * @throws Exception e
     */
    private Object createInstance(String beanName) throws Exception {
        return this.beanDefinitionMap.get(beanName).newInstance();
    }

    /**
     * 动态代理(反射)，注入切面，进行通知
     *
     * @param bean 原始的Bean
     * @return 注入切面后的Bean
     */
    private Object proxyEnhance(Object bean) {
        // 判断是否要将切面注入
        if (this.aspect != null && this.aspect.getPointcut().matchClass(bean.getClass())) {
            return Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(),
                    new AopInvocationHandler(bean, aspect));
        }
        return bean;
    }

    public Aspect getAspect() {
        return aspect;
    }

    public void setAspect(Aspect aspect) {
        this.aspect = aspect;
    }
}
