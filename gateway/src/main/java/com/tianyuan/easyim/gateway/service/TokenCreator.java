package com.tianyuan.easyim.gateway.service;

import com.tianyuan.easyim.common.model.TokenData;
import com.tianyuan.easyim.common.token.TokenGenerator;
import com.tianyuan.easyim.gateway.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenCreator {

    private static final int ONE_MINUTE = 60;

    private final TokenGenerator tokenGenerator;

    public String create(User user) {
        TokenData tokenData = new TokenData(user.getUsername(), System.currentTimeMillis());
        return tokenGenerator.newJwt(tokenData, ONE_MINUTE);
    }
}
