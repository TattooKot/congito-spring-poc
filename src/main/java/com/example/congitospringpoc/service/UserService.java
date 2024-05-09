package com.example.congitospringpoc.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ChangePasswordRequest;
import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.example.congitospringpoc.config.CognitoProperties;
import com.example.congitospringpoc.dto.UserPasswordChangingDto;
import com.example.congitospringpoc.dto.UserRegistrationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private static final String AUTH_FLOW = "USER_PASSWORD_AUTH";
    private static final String USERNAME_PARAM = "USERNAME";
    private static final String PASSWORD_PARAM = "PASSWORD";
    private static final String EMAIL_PARAM = "email";
    private final CognitoProperties cognitoProperties;
    private final AWSCognitoIdentityProvider cognitoIdentityProvider;

    public String changePassword(UserPasswordChangingDto userPasswordChangingDto) {

        // Create InitiateAuthRequest
        var authResult = getInitiateAuth(userPasswordChangingDto);

        // Extract and return Access Token
        var accessToken = getAccessToken(authResult);


        var changePasswordResult = changePasswordRequest(userPasswordChangingDto, accessToken);
        return changePasswordResult.toString();
    }

    private ChangePasswordResult changePasswordRequest(UserPasswordChangingDto userPasswordChangingDto, String accessToken) {
        var changePasswordRequest = new ChangePasswordRequest()
                .withAccessToken(accessToken)
                .withPreviousPassword(userPasswordChangingDto.getOldPassword())
                .withProposedPassword(userPasswordChangingDto.getNewPassword());

        // Change the password
        return cognitoIdentityProvider.changePassword(changePasswordRequest);
    }

    private static String getAccessToken(InitiateAuthResult authResult) {
        return Optional.ofNullable(authResult)
                .map(InitiateAuthResult::getAuthenticationResult)
                .map(AuthenticationResultType::getAccessToken)
                .orElseThrow();
    }

    private InitiateAuthResult getInitiateAuth(UserPasswordChangingDto userPasswordChangingDto) {
        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withAuthFlow(AUTH_FLOW)
//                .withClientId(clientId)
                .withAuthParameters(
                        Map.of(
                                USERNAME_PARAM, userPasswordChangingDto.getUsername(),
                                PASSWORD_PARAM, userPasswordChangingDto.getOldPassword()
                        )
                );

        // Initiate authentication
        return cognitoIdentityProvider.initiateAuth(authRequest);
    }

    public String userRegister(UserRegistrationDto userDto) {
        var request = new AdminCreateUserRequest()
                .withUserPoolId(cognitoProperties.getPoolId())
                .withUsername(userDto.getUsername())
                .withUserAttributes(
                        new AttributeType().withName(EMAIL_PARAM).withValue(userDto.getEmail())
//                        new AttributeType().withName("custom:role").withValue(userDto.getRole())
                        // Add other attributes as needed
                );

        var createdUser = cognitoIdentityProvider.adminCreateUser(request);
        return getCreatedUsername(createdUser);
    }

    public AdminGetUserResult getUser(String username) {
        var adminGetUserRequest = new AdminGetUserRequest()
                .withUserPoolId(cognitoProperties.getPoolId())
                .withUsername(username);

        return cognitoIdentityProvider.adminGetUser(adminGetUserRequest);
    }

    public String getUserStatus(AdminGetUserResult user) {
        return Optional.ofNullable(user)
                .map(AdminGetUserResult::getUserStatus)
                .orElseThrow();
    }

    private String getCreatedUsername(AdminCreateUserResult createdUser) {
        return Optional.ofNullable(createdUser)
                .map(AdminCreateUserResult::getUser)
                .map(UserType::getUsername)
                .orElseThrow();
    }
}
