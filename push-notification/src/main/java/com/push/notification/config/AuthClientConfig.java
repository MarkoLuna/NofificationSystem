package com.push.notification.config;

import com.notification.api.client.AuthorizationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class AuthClientConfig {

    @Value("${auth-service.host}")
    private String authServiceHost;

    @Bean
    public AuthorizationClient authorizationClient(ObjectMapper mapper) {
        return new AuthorizationClient(authServiceHost, mapper);
    }
}
