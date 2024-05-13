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
        return userService.login(userLoginDto);
    }

    @PatchMapping("/password")
    public String passwordChanging(@RequestBody UserPasswordChangingDto userPasswordChangingDto) {
        return userService.changePassword(userPasswordChangingDto);
    }

    @PatchMapping("/first-password")
    public String firstPasswordChanging(@RequestBody UserPasswordChangingDto userPasswordChangingDto) {
        return userService.firstChangePassword(userPasswordChangingDto);
    }
}
