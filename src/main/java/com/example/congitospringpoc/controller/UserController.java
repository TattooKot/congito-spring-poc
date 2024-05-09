package com.example.congitospringpoc.controller;

import com.example.congitospringpoc.dto.UserLoginDto;
import com.example.congitospringpoc.dto.UserPasswordChangingDto;
import com.example.congitospringpoc.dto.UserRegistrationDto;
import com.example.congitospringpoc.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {


    private final UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserRegistrationDto userDto) {
        var newUsername = userService.userRegister(userDto);
        return "User registered successfully: " + newUsername;
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserLoginDto userLoginDto) {
        var user = userService.getUser(userLoginDto.getUsername());
        var userStatus = userService.getUserStatus(user);

        return switch (userStatus) {
            case "FORCE_CHANGE_PASSWORD" ->
                    "you should change your password. Redirection to page with password changing";
            default -> loginPage(userLoginDto);
        };
    }

    @PatchMapping("/password")
    public String passwordChanging(UserPasswordChangingDto userPasswordChangingDto) {

        return "";
    }

    private String loginPage(UserLoginDto userLoginDto) {
        return "login successful for user: " + userLoginDto.getUsername();
    }
}
