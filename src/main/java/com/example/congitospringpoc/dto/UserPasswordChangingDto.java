package com.example.congitospringpoc.dto;

import lombok.Data;

@Data
public class UserPasswordChangingDto {

    private String username;
    private String oldPassword;
    private String newPassword;
    private String accessToken;
}
