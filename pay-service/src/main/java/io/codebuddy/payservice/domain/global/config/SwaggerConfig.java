package io.codebuddy.payservice.domain.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        // Bearer Token 설정
                        .addSecuritySchemes("Bearer Token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))

                        // X-USER-ID 헤더 설정
                        .addSecuritySchemes("user-id",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-USER-ID")
                                        .description("Gateway에서 주입되는 User ID (예: 1)"))

                        // X-USER-ROLE 헤더 설정
                        .addSecuritySchemes("user-role",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-USER-ROLE")
                                        .description("Gateway에서 주입되는 User Role (예: MEMBER)")))

                // 3가지 설정을 모든 API에 전역으로 적용
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearer-key")
                        .addList("user-id")
                        .addList("user-role"))

                .info(new Info()
                        .title("Pay Service API")
                        .description("ClosetBuddy의 Pay Service API 명세서입니다.")
                        .version("v0"));
    }

}
