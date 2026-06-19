//package com.hospital.Arogeva.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .addServersItem(new Server()
//                        .url("http://localhost:8082")
//                        .description("Local Development Server"))
//                .addServersItem(new Server()
//                        .url("https://192.168.10.73:8082")
//                        .description("Production Server"))
//                .info(new Info()
//                        .title("Arogeva V2 API")
//                        .version("1.0")
//                        .description("API Documentation for Arogeva V2 Effort Tracker"))
//                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
//                .components(new Components()
//                        .addSecuritySchemes("BearerAuth",
//                                new SecurityScheme()
//                                        .name("BearerAuth")
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")));
//    }
//}
package com.hospital.Arogeva.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(new Info()
                        .title("Arogeva V2 API")
                        .version("1.0")
                        .description("API Documentation for Arogeva V2 Effort Tracker"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .name("BearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
