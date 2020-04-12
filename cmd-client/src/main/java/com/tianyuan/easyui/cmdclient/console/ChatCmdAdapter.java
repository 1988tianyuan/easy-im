package com.tianyuan.easyui.cmdclient.console;

import io.netty.channel.Channel;

public interface ChatCmdAdapter {

    void chatRequest(String msg, Channel channel);
}
