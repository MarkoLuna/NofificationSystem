package com.email.notification.config;

import com.email.notification.config.properties.ClientCredentialsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ClientCredentialsProperties.class)
public class OAuthConfig {
}
