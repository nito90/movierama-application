package com.toulios.projects.movierama;

import com.google.common.base.Predicates;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Configuration for Swagger
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = MovieramaApplication.class)
public class SwaggerConfig {

    @SuppressWarnings("unchecked")
    @Bean
    public Docket swagger2Configuration() {

        Contact contact = new Contact("Nikos Toulios", StringUtils.EMPTY, "toulios90@gmail.com");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Toulios Exercise")
                        .description("This api is for a test exercise.")
                        .version("0.1")
                        .contact(contact)
                        .build())
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors.basePackage("com.toulios.projects.movierama")))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalDateTime.class, String.class)
                .directModelSubstitute(ZonedDateTime.class, String.class)
                .useDefaultResponseMessages(false);
    }
}
