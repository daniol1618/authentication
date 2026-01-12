package com.gl.authentication.controller;

import com.gl.authentication.dto.UserResponse;
import com.gl.authentication.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public UserResponse login(@RequestHeader("Authorization") String auth) {
        String token = auth.replace("Bearer ", "");
        return userService.login(token);
    }
}