package com.tianyuan.easyui.cmdclient.login;

import com.tianyuan.easyim.common.model.LoginResponse;
import com.tianyuan.easyim.common.util.JsonUtil;
import com.tianyuan.easyui.cmdclient.http.HttpRequestUtil;

import java.util.Collections;

public class LoginHandler {

    public static LoginResponse login(String username) {
        // TODO: configurable
        String loginUrl = "http://127.0.0.1:8080/gateway/users/login";
        String loginResp = HttpRequestUtil.jsonPostRequest(loginUrl, Collections.singletonMap("username", username));
        return JsonUtil.fromJson(loginResp, LoginResponse.class);
    }
}
