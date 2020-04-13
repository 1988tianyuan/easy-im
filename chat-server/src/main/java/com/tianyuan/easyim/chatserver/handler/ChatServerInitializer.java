package com.tianyuan.easyim.chatserver.handler;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServerInitializer extends ChannelInitializer<NioServerSocketChannel> {
    
    // TODO: configurable
    private static final String zkConnectString = "localhost:2181";

    @Override
    protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
        log.debug("chat-server启动中");
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkConnectString, new RetryForever(1000));
        client.start();
        // TODO: register to zk, and other possible init process.
    }
}
