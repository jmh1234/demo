package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created with IntelliJ IDEA.
 * Swagger2配置
 *
 * @author Ji MingHao
 * @since 2020-6-9 19:37
 */
@Configuration
public class Swagger2Config {

    /**
     * 创建Rest Api描述
     *
     * @return Docket Docket
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 你需要生成文档所在的包
                .apis(RequestHandlerSelectors.basePackage("com.demo.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 接口描述
     *
     * @return ApiInfo ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title("服务API文档")
                .contact(new Contact("季明浩", "", "9232686771@qq.com"))
                .description("服务API文档")
                .termsOfServiceUrl("demo")
                .version("1.0")
                .build();
    }
}
