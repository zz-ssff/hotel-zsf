package com.hotel.pms.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "酒店PMS系统API",
        version = "1.0.0",
        description = "酒店物业管理系统API文档"
    ),
    servers = @Server(
        url = "/",
        description = "本地开发服务器"
    )
)
public class OpenApiConfig {
}
