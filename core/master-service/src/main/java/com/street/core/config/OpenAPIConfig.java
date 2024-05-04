package com.street.core.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class OpenAPIConfig {

    @Value("${my-config.openapi.dev-url}")
    private String devUrl;

    @Value("${my-config.openapi.prod-url}")
    private String prodUrl;

    @Value("${my-config.openapi.local-url}")
    private String localUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl(localUrl);
        localServer.setDescription("Local Server URL in Development environment");

        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("DEV Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("PROD Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("jenny.maya186@gmail.com");
        contact.setName("Jenny ( Senior Java Engineer )");
        contact.setUrl("https://www.jenny.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Crypto Payment Gateway Syatem")
                .version("1.0")
                .contact(contact)
                .description("The multiple Crypto Payment Gateway Syatem.").termsOfService("https://www.jenny.com/terms")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(localServer, devServer, prodServer));
    }
}
