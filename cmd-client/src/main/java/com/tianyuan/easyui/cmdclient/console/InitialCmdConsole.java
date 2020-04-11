package com.tianyuan.easyui.cmdclient.console;

import com.tianyuan.easyim.common.protocal.ChatMsgUtil;
import io.netty.channel.Channel;

import java.util.Scanner;

import static com.tianyuan.easyim.common.model.IMMsg.*;

public class InitialCmdConsole {

    public void exec(Scanner scanner, Channel channel) {
        String msg = scanner.next();
        // TODO: create valid msg with sessionId
        ChatRequestMsg requestMsg = ChatRequestMsg
                .newBuilder()
                .setUsername("刘耕")
                .setTargetUsername("某某某")
                .setMessage(msg).build();
        BaseRequestMsg baseRequestMsg = ChatMsgUtil.createBaseMsg(RequestType.ChatRequest, requestMsg);
        channel.writeAndFlush(baseRequestMsg);
    }
}
