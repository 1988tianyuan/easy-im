package com.tianyuan.easyui.cmdclient.chat;

import static com.tianyuan.easyui.cmdclient.chat.ChatConstant.*;

import org.apache.commons.lang3.StringUtils;

import com.tianyuan.easyui.cmdclient.handler.ChatClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServerConnector {
    
    private final NioEventLoopGroup eventLoopGroup;
    
    public ChatServerConnector() {
        eventLoopGroup = new NioEventLoopGroup();
    }
    
    public Channel start(String hostAndPort, String username) throws Exception {
        String host = StringUtils.split(hostAndPort, ":")[0];
        int port = Integer.parseInt(StringUtils.split(hostAndPort, ":")[1]);
        Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChatClientInitializer())
                .attr(usernameAttrKey, username)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        return bootstrap.connect(host, port).sync().channel();
    }
    
    public synchronized void shutdown() {
        eventLoopGroup.shutdownGracefully();
    }
}
