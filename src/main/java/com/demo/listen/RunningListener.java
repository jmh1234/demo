package com.demo.listen;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 启动监听器
 *
 * @author Ji MingHao
 * @since 2022-05-09 16:54
 */
@Component
public class RunningListener implements ApplicationRunner {

    @Resource
    ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println("============> " + name);
        }
        final List<String> k1 = args.getOptionValues("k1");
        if (k1 != null && !k1.isEmpty()) {
            System.out.println(k1.get(0));
        }
    }
}
