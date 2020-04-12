package com.tianyuan.easyui.cmdclient.console;

import com.tianyuan.easyim.common.model.LoginResponse;
import com.tianyuan.easyim.common.util.JsonUtil;
import com.tianyuan.easyui.cmdclient.http.HttpRequestUtil;

import java.util.Collections;
import java.util.Scanner;

public class LoginConsole implements CmdConsole {

    @Override
    public void exec(ConsoleCommand cmd) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入username： ");
        String username = scanner.nextLine();
        // TODO: configurable
        String loginUrl = "http://127.0.0.1:8080/gateway/users/login";
        String loginResp = HttpRequestUtil.jsonPostRequest(loginUrl, Collections.singletonMap("username", username));
        LoginResponse loginResponse = JsonUtil.fromJson(loginResp, LoginResponse.class);
    }
}
