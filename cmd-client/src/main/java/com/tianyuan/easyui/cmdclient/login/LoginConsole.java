package com.tianyuan.easyui.cmdclient.login;

import static com.tianyuan.easyim.common.model.CommonConstant.*;

import java.util.Collections;
import java.util.Scanner;

import com.tianyuan.easyim.common.model.LoginResponse;
import com.tianyuan.easyim.common.util.JsonUtil;
import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.chat.ClientStatus;
import com.tianyuan.easyui.cmdclient.console.CmdConsole;
import com.tianyuan.easyui.cmdclient.http.HttpRequestUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginConsole implements CmdConsole {
    
    private final ChatContext chatContext;
    
    public LoginConsole(ChatContext chatContext) {
        this.chatContext = chatContext;
    }
    
    @Override
    public void exec(Scanner scanner) {
        System.out.println("Please enter username to login: ");
        String username = scanner.next();
        try {
            login(username);
            log.debug("Success to login with username: {}", username);
            System.out.println("Success to login, enter use format ':targetUserName message' to begin a chat with targetUser");
            //TODO: begin a new chat loop to handle chat command
        } catch (Exception e) {
            log.error("Error happens when login with username: {}", username, e);
            System.out.println("Failed to login, please try again.");
        }
    }
    
    private void login(String username) throws Exception {
        // TODO: loginUrl should be configurable
        String loginUrl = "http://127.0.0.1:8080/gateway/users/login";
        String loginResp = HttpRequestUtil.jsonPostRequest(loginUrl, Collections.singletonMap(USER_NAME, username));
        LoginResponse loginResponse = JsonUtil.fromJson(loginResp, LoginResponse.class);
        Channel channel = chatContext.getChatServerConnector().start(loginResponse.getServerAddress(), username);
        // TODO: send connectCreateRequest msg with JWT to chat-server then check whether session is valid 
        chatContext.setChatChannel(channel);
        chatContext.setUsername(username);
        chatContext.setStatus(ClientStatus.LOGGED_IN);
    }
}
