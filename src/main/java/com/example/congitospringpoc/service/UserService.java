package com.example.congitospringpoc.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.example.congitospringpoc.dto.UserLoginDto;
import com.example.congitospringpoc.dto.UserPasswordChangingDto;
import com.example.congitospringpoc.dto.UserRegistrationDto;
import com.example.congitospringpoc.utils.RequestsBuilder;
import com.example.congitospringpoc.utils.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final AWSCognitoIdentityProvider cognitoIdentityProvider;
    private final RequestsBuilder requestsBuilder;

    public String changePassword(UserPasswordChangingDto userPasswordChangingDto) {
        Optional.ofNullable(userPasswordChangingDto)
                .map(requestsBuilder::getChangePasswordRequest)
                .ifPresent(cognitoIdentityProvider::changePassword);
        return "Password successfully changed!";
    }

    public String login(UserLoginDto userLoginDto) {
        return Optional.ofNullable(userLoginDto)
                .map(dto -> getInitiateAuth(userLoginDto.getUsername(), userLoginDto.getPassword()))
                .map(UserUtils::getAccessToken)
                .orElseThrow();
    }

    public String firstChangePassword(UserPasswordChangingDto userPasswordChangingDto) {
        var authResult = getInitiateAuth(userPasswordChangingDto.getUsername(), userPasswordChangingDto.getOldPassword());
        var changedPasswordResult = changePasswordForTheFirstTime(authResult, userPasswordChangingDto);
        return changedPasswordResult.getAuthenticationResult().getAccessToken();
    }

    private AdminRespondToAuthChallengeResult changePasswordForTheFirstTime(InitiateAuthResult authResult, UserPasswordChangingDto userPasswordChangingDto) {
        var changeRequest = requestsBuilder.getAdminRespondToAuthChallengeRequest(userPasswordChangingDto, authResult);
        return cognitoIdentityProvider.adminRespondToAuthChallenge(changeRequest);
    }

    private InitiateAuthResult getInitiateAuth(String username, String password) {
        var authRequest = requestsBuilder.getInitiateAuthRequest(username, password);
        return cognitoIdentityProvider.initiateAuth(authRequest);
    }

    public String userRegister(UserRegistrationDto userDto) {
        return Optional.ofNullable(userDto)
                .map(requestsBuilder::getCreateUserRequest)
                .map(cognitoIdentityProvider::adminCreateUser)
                .map(UserUtils::getCreatedUsername)
                .orElseThrow();
    }

    public AdminGetUserResult getUser(String username) {
        return Optional.ofNullable(username)
                .map(requestsBuilder::getUserRequest)
                .map(cognitoIdentityProvider::adminGetUser)
                .orElseThrow();
    }
}
