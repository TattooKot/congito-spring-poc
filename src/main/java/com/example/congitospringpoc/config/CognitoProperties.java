package com.example.congitospringpoc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "aws.cognito")
public class CognitoProperties {

    private String secretKey;
    private String accessKey;
    private String poolId;
    private String clientId;
}
