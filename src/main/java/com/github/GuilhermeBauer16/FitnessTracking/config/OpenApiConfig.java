package com.github.GuilhermeBauer16.FitnessTracking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Fitness Tracking API")
                        .version("v1")
                        .description("A Fitness Tracking API")
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("https://springdoc.org/")
                        ));
    }
}
