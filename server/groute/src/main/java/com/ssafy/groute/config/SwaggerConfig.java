package com.ssafy.groute.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String version;
    private String title;

    @Bean
    public Docket apiV1() {
        version = "V1";
        title = "GROUTE API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ssafy.groute.controller"))
                .paths(PathSelectors.ant("/groute/**"))
                .build()
                .apiInfo(apiInfo(title, version));

    }


    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
                title,
                "GROUTE API",
                version,
                "www.example.com",
                new Contact("Contact Me", "www.example.com", "test@example.com"),
                "Licenses",
                "www.example.com",
                new ArrayList<>());
    }
}
