package com.sms.sms.config;

import com.sms.sms.config.properties.ClientCredentialsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ClientCredentialsProperties.class)
public class OAuthConfig {
}
