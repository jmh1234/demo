package com.test.demo.caseTest.aop.aopdemo;

import com.test.demo.caseTest.aop.aop.Aspect;
import com.test.demo.caseTest.aop.aop.IocContainer;
import com.test.demo.caseTest.aop.aop.Pointcut;
import com.test.demo.caseTest.aop.service.KtvPrincessB;
import com.test.demo.caseTest.aop.service.KtvService;
import com.test.demo.caseTest.aop.service.SPAService;
import com.test.demo.caseTest.aop.service.SpaPrincessA;

public class AopMain {

    public static void main(String[] args) throws Exception {
        IocContainer ioc = new IocContainer();
        // 手动添加IOC容器和Bean的依赖
        ioc.addBean("spa", SpaPrincessA.class);
        ioc.addBean("ktv", KtvPrincessB.class);

        String classPattern = "com\\.example\\.aop\\.service\\..*"; // 注入切面的路径的正则表达式
        String methodPattern = ".*Message"; // 注入切面的方法的正则表达式
        Pointcut pointcut = new Pointcut(classPattern, methodPattern);
        Aspect asp = new Aspect(pointcut, new TimeCsAdvice());
        ioc.setAspect(asp);

        SPAService spa = (SPAService) ioc.getBean("spa");
        spa.aromaOilMessage("mike");
        spa.rest();

        KtvService ktv = (KtvService) ioc.getBean("ktv");
        ktv.momoSingMessage("mike");
    }
}
