package com.example.congitospringpoc.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class CognitoConfig {

    private final CognitoProperties cognitoProperties;

    @Bean
    public AWSCognitoIdentityProvider cognitoIdentityProvider() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(cognitoProperties.getAccessKey(), cognitoProperties.getSecretKey());
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.EU_NORTH_1)
                .build();
    }

}
