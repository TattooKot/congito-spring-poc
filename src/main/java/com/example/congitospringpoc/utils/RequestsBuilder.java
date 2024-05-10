package com.example.congitospringpoc.utils;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.ChangePasswordRequest;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.example.congitospringpoc.config.CognitoProperties;
import com.example.congitospringpoc.dto.UserPasswordChangingDto;
import com.example.congitospringpoc.dto.UserRegistrationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class RequestsBuilder {


    private static final String AUTH_FLOW = "USER_PASSWORD_AUTH";
    private static final String USERNAME_PARAM = "USERNAME";
    private static final String PASSWORD_PARAM = "PASSWORD";
    private static final String NEW_PASSWORD_PARAM = "NEW_PASSWORD";
    private static final String EMAIL_PARAM = "email";
    private final CognitoProperties cognitoProperties;

    public AdminCreateUserRequest getCreateUserRequest(UserRegistrationDto userDto) {
        return new AdminCreateUserRequest()
                .withUserPoolId(cognitoProperties.getPoolId())
                .withUsername(userDto.getUsername())
                .withUserAttributes(
                        new AttributeType().withName(EMAIL_PARAM).withValue(userDto.getEmail())
                        // Add other attributes as needed
                        // new AttributeType().withName("custom:role").withValue(userDto.getRole())
                );
    }

    public AdminGetUserRequest getUserRequest(String username) {
        return new AdminGetUserRequest()
                .withUserPoolId(cognitoProperties.getPoolId())
                .withUsername(username);
    }

    public InitiateAuthRequest getInitiateAuthRequest(String username, String password) {
        return new InitiateAuthRequest()
                .withAuthFlow(AUTH_FLOW)
                .withClientId(cognitoProperties.getClientId())
                .withAuthParameters(
                        Map.of(
                                USERNAME_PARAM, username,
                                PASSWORD_PARAM, password
                        )
                );

    }

    public ChangePasswordRequest getChangePasswordRequest(UserPasswordChangingDto userPasswordChangingDto) {
        return new ChangePasswordRequest()
                .withAccessToken(userPasswordChangingDto.getAccessToken())
                .withPreviousPassword(userPasswordChangingDto.getOldPassword())
                .withProposedPassword(userPasswordChangingDto.getNewPassword());

    }

    public AdminRespondToAuthChallengeRequest getAdminRespondToAuthChallengeRequest(UserPasswordChangingDto userPasswordChangingDto, InitiateAuthResult authResult) {
        return new AdminRespondToAuthChallengeRequest()
                .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                .withChallengeResponses(Map.of(

                        USERNAME_PARAM, userPasswordChangingDto.getUsername(),
                        PASSWORD_PARAM, userPasswordChangingDto.getOldPassword(),
                        NEW_PASSWORD_PARAM, userPasswordChangingDto.getNewPassword()
                ))
                .withClientId(cognitoProperties.getClientId())
                .withUserPoolId(cognitoProperties.getPoolId())
                .withSession(authResult.getSession());
    }
}
