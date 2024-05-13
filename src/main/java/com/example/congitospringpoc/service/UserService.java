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

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private static final String FORCE_CHANGE_PASSWORD_STATUS = "FORCE_CHANGE_PASSWORD";
    private final AWSCognitoIdentityProvider cognitoIdentityProvider;
    private final RequestsBuilder requestsBuilder;

    public String changePassword(UserPasswordChangingDto userPasswordChangingDto) {
        Optional.ofNullable(userPasswordChangingDto)
                .map(requestsBuilder::getChangePasswordRequest)
                .ifPresent(cognitoIdentityProvider::changePassword);
        return "Password successfully changed!";
    }

    public String login(UserLoginDto userLoginDto) {
        if(isPasswordChangingRequired(userLoginDto)) {
            return "you should change your password. Redirection to page with password changing";
        }

        return Optional.of(userLoginDto)
                .map(dto -> getInitiateAuth(userLoginDto.getUsername(), userLoginDto.getPassword()))
                .map(UserUtils::getAccessToken)
                .orElseThrow();
    }

    private boolean isPasswordChangingRequired(UserLoginDto userLoginDto) {
        return Optional.ofNullable(userLoginDto.getUsername())
                .map(this::checkUserStatus)
                .filter(status -> Objects.equals(status, FORCE_CHANGE_PASSWORD_STATUS))
                .isPresent();
    }

    private String checkUserStatus(String username) {
        return Optional.ofNullable(username)
                .map(this::getUser)
                .map(UserUtils::getUserStatus)
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
