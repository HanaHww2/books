package com.book.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {

    Info info = new Info()
        .title("Book Search API")
        .description("도서 검색 기능을 제공하는 API docs")
        .version("1.0.0");

    return new OpenAPI()
        .info(info);
  }
}