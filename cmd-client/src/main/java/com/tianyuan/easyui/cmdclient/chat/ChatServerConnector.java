package com.tianyuan.easyui.cmdclient.chat;

import static com.tianyuan.easyui.cmdclient.chat.ChatConstant.*;

import org.apache.commons.lang3.StringUtils;

import com.tianyuan.easyui.cmdclient.handler.ChatClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServerConnector {
    
    public static Channel start(String hostAndPort, String username, ChatContext chatContext) throws Exception {
        String host = StringUtils.split(hostAndPort, ":")[0];
        int port = Integer.parseInt(StringUtils.split(hostAndPort, ":")[1]);
        Bootstrap bootstrap = new Bootstrap().group(chatContext.getEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChatClientInitializer(chatContext))
                .attr(usernameAttrKey, username)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        return bootstrap.connect(host, port).sync().channel();
    }
}
