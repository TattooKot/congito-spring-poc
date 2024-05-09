package com.example.congitospringpoc.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({CognitoProperties.class})
public class AppConfig {
}
