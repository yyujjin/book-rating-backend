package com.example.bookrating.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${backend.url}")
    private String backendUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        // 서버 URL 설정
        Server server = new Server();
        server.setUrl(backendUrl);
        server.setDescription("aws ec2");

        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("Book Rating API")
                        .version("1.0.0")
                        .description("Book Rating 애플리케이션의 API 문서입니다.")
                );
    }
}