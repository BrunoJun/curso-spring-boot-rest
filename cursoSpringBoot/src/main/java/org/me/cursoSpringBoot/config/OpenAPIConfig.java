package org.me.cursoSpringBoot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI(){

        return new OpenAPI()
                .info(new Info()
                        .title("Restful API with Java 17 and Spring Boot 3")
                        .version("v1")
                        .description("Projeto do curso de Spring Boot 3.0 com Java 17")
                        .termsOfService("www.termos.com")
                        .license(new License().name("Apache 2.0").url("www.minhalicensa.com")));
    }
}
