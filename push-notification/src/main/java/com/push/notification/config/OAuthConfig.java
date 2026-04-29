package com.push.notification.config;

import com.push.notification.config.properties.ClientCredentialsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ClientCredentialsProperties.class)
public class OAuthConfig {
}
