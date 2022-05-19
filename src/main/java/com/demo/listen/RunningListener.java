package com.demo.listen;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * 启动监听器
 *
 * @author Ji MingHao
 * @since 2022-05-09 16:54
 */
@Log4j2
@Component
public class RunningListener implements ApplicationRunner {

    @Resource
    ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            final String name = beanDefinitionNames[i];
            if (name.toLowerCase().contains("naco")) {
                log.info("nacos相关服务{}：{}", i, name);
            }
        }
        log.info("启动时传入的参数为：{}", args.getOptionValues("k1"));
    }
}
