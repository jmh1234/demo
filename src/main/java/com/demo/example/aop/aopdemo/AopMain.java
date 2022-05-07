package com.demo.example.aop.aopdemo;

import com.demo.example.aop.aop.Aspect;
import com.demo.example.aop.aop.IocContainer;
import com.demo.example.aop.aop.Pointcut;
import com.demo.example.aop.service.KtvPrincessImpl;
import com.demo.example.aop.service.KtvService;
import com.demo.example.aop.service.SpaService;
import com.demo.example.aop.service.SpaPrincessImpl;

/**
 * Created with IntelliJ IDEA.
 * 切面主类
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class AopMain {

    public static void main(String[] args) throws Exception {
        IocContainer ioc = new IocContainer();
        // 手动添加IOC容器和Bean的依赖
        ioc.addBean("spa", SpaPrincessImpl.class);
        ioc.addBean("ktv", KtvPrincessImpl.class);
        // 注入切面的路径的正则表达式
        String classPattern = "com\\.example\\.aop\\.service\\..*";
        // 注入切面的方法的正则表达式
        String methodPattern = ".*Message";
        Pointcut pointcut = new Pointcut(classPattern, methodPattern);
        Aspect asp = new Aspect(pointcut, new TimeCsAdvice());
        ioc.setAspect(asp);
        SpaService spa = (SpaService) ioc.getBean("spa");
        spa.aromaOilMessage("mike");
        spa.rest();
        KtvService ktv = (KtvService) ioc.getBean("ktv");
        ktv.momoSingMessage("mike");
    }
}
