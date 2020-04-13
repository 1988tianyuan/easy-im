package com.tianyuan.easyim.gateway.application;

import com.tianyuan.easyim.common.model.LoginResponse;
import com.tianyuan.easyim.gateway.model.User;
import com.tianyuan.easyim.gateway.service.TokenCreator;
import com.tianyuan.easyim.gateway.service.server.ServerAssigner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginApp {

    private final TokenCreator tokenCreator;
    private final ServerAssigner serverAssigner;

    public LoginResponse login(User user) {
        String token = tokenCreator.create(user);
//        Server server = serverAssigner.assign(user)
//                .orElseThrow(() -> new NoServerAvailableException("No server for user: " + user));

        return new LoginResponse(token, "127.0.0.1:8000");
    }
}
