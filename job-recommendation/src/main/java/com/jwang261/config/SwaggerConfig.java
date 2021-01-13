package com.jwang261.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
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
    @Bean
    public Docket docket(Environment environment){
        Profiles profiles = Profiles.of("dev", "test");
        boolean isDev = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(isDev)
                .groupName("jwang261")
                .select()
                .apis(RequestHandlerSelectors.any())
                .build();
    }
    private ApiInfo apiInfo(){
        Contact contact = new Contact("jwang261","http://www.littlepeachie.com","jwang261@syr.edu");
        return new ApiInfo("SwaggerAPI文档",
                "这是我的Recommendation System SwaggerAPI文档",
                "1.0",
                "http://www.littlepeachie.com",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
