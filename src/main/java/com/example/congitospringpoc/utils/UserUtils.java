package com.example.congitospringpoc.utils;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.UserType;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class UserUtils {

    public static String getAccessToken(InitiateAuthResult authResult) {
        return Optional.ofNullable(authResult)
                .map(InitiateAuthResult::getAuthenticationResult)
                .map(AuthenticationResultType::getAccessToken)
                .orElseThrow();
    }

    public static String getUserStatus(AdminGetUserResult user) {
        return Optional.ofNullable(user)
                .map(AdminGetUserResult::getUserStatus)
                .orElseThrow();
    }

    public static String getCreatedUsername(AdminCreateUserResult createdUser) {
        return Optional.ofNullable(createdUser)
                .map(AdminCreateUserResult::getUser)
                .map(UserType::getUsername)
                .orElseThrow();
    }
}
