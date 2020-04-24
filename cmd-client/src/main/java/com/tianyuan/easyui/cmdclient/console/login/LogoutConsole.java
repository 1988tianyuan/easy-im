package com.tianyuan.easyui.cmdclient.console.login;

import java.util.Scanner;

import com.tianyuan.easyim.common.model.IMMsg;
import com.tianyuan.easyim.common.protocal.ChatMsgUtil;
import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.console.CmdConsole;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogoutConsole implements CmdConsole {
    
    private final ChatContext chatContext;
    
    public LogoutConsole(ChatContext chatContext) {
        this.chatContext = chatContext;
    }
    
    @Override
    public void exec(Scanner scanner) {
        try {
            logout(chatContext.getSessionId());
        } catch (Exception e) {
            log.error("Error happens when [user:{},sessionId:{}] logout.", chatContext.getUsername(), chatContext.getSessionId(), e);
        } finally {
            chatContext.logoutClear();
            System.out.println("Success to logout.");
        }
    }
    
    private void logout(String sessionId) throws Exception {
        IMMsg.BaseRequestMsg requestMsg = ChatMsgUtil.createBaseMsg(IMMsg.RequestType.FinishSessionRequest,
            IMMsg.FinishSessionRequestMsg.newBuilder().setSessionId(sessionId).build());
        chatContext.getChatChannel().writeAndFlush(requestMsg).sync();
    }
}
