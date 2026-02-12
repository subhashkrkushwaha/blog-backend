package com.example.blog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCoustomConfig() {
        return new OpenAPI().info(
                new Info().title("Blog API").description("Blog API")
        )
                .servers(List.of(new Server().url("http://localhost:8080").description("Local"),
                        new Server().url("http://localhost:8081").description("Live")))
                .tags(List.of(
                        new Tag().name("AUTH API"),
                        new Tag().name("User Role API"),
                        new Tag().name("User API"),
                        new Tag().name("Category API"),
                        new Tag().name("Post API"),
                        new  Tag().name("Comment API")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth",new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));

    }

}
