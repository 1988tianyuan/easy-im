package com.tianyuan.easyim.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServerInitializer extends ChannelInitializer<NioServerSocketChannel> {

    @Override
    protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
        log.debug("chat-server启动中");
        // TODO: register to zk, and other possible init process.
    }
}
