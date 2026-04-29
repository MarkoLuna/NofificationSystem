package com.email.notification.config;

import com.email.notification.config.properties.ClientCredentialsProperties;
import com.notification.api.client.AuthorizationClient;
import com.notification.api.client.UsersClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class UserClientConfig {

    @Value("${users-service.host}")
    private String userServiceHost;

    @Bean
    public UsersClient usersClient(AuthorizationClient authorizationClient,
                                   ObjectMapper objectMapper,
                                   ClientCredentialsProperties clientCredentialsProperties) {

        return new UsersClient(userServiceHost,
                objectMapper,
                authorizationClient,
                clientCredentialsProperties.getId(),
                clientCredentialsProperties.getSecret());
    }
}
