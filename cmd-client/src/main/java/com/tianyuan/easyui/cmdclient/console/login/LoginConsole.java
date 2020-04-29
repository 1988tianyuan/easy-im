package com.tianyuan.easyui.cmdclient.console.login;

import static com.tianyuan.easyim.common.model.CommonConstant.*;

import java.util.Collections;
import java.util.Scanner;

import com.tianyuan.easyim.common.model.IMMsg;
import com.tianyuan.easyim.common.model.LoginResponse;
import com.tianyuan.easyim.common.protocal.ChatMsgUtil;
import com.tianyuan.easyim.common.util.JsonUtil;
import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.chat.ChatServerConnector;
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
        } catch (Exception e) {
            log.error("Error happens when login with username: {}", username, e);
            System.out.println("Failed to login, please try again.");
        }
    }
    
    private void login(String username) throws Exception {
        chatContext.setUsername(username);
        LoginResponse loginResponse = sendLoginRequestToGateway(username);
        Channel channel = ChatServerConnector.start(loginResponse.getServerAddress(), username, chatContext);
        sendCreateSessionRequest(channel, loginResponse, username);
        chatContext.setChatChannel(channel);
        chatContext.setStatus(ClientStatus.LOGGING_IN);
        System.out.println("logging in, please wait......");
    }
    
    private LoginResponse sendLoginRequestToGateway(String username) {
        // TODO: loginUrl should be configurable
        String loginUrl = "http://127.0.0.1:8080/gateway/users/login";
        String loginResp = HttpRequestUtil.jsonPostRequest(loginUrl, Collections.singletonMap(USER_NAME, username));
        return JsonUtil.fromJson(loginResp, LoginResponse.class);
    }
    
    private void sendCreateSessionRequest(Channel channel, LoginResponse loginResponse, String username) throws Exception {
        IMMsg.BaseRequestMsg requestMsg = ChatMsgUtil.createBaseMsg(IMMsg.RequestType.CreateSessionRequest, 
            IMMsg.SessionCreateRequestMsg.newBuilder()
            .setUsername(username)
            .setJwt(loginResponse.getToken())
            .build());
        channel.writeAndFlush(requestMsg).addListener(future -> {
            if (!future.isSuccess()) {
                System.out.println("Failed to login, please try again.");
                channel.close();
                chatContext.setChatChannel(null);
                chatContext.setStatus(ClientStatus.INIT);
            }
        });
    }
}
