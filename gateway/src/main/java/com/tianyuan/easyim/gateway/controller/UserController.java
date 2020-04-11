package com.tianyuan.easyim.gateway.controller;

import com.tianyuan.easyim.gateway.application.LoginApp;
import com.tianyuan.easyim.gateway.controller.request.LoginRequest;
import com.tianyuan.easyim.gateway.controller.response.LoginResponse;
import com.tianyuan.easyim.gateway.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final LoginApp loginApp;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        User user = new User(request.getUsername());
        return loginApp.login(user);
    }
}
