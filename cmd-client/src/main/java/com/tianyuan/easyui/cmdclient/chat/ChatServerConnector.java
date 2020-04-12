package com.tianyuan.easyui.cmdclient.chat;

import com.tianyuan.easyui.cmdclient.console.InitialCmdConsole;
import com.tianyuan.easyui.cmdclient.handler.ChatClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class ChatServerConnector {

    public Channel start(String serverAddress) throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap().group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChatClientInitializer())
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        return connect(bootstrap, serverAddress);
    }

    private Channel connect(Bootstrap bootstrap, String serverAddress) throws InterruptedException {
        return bootstrap.connect(new DomainSocketAddress(serverAddress)).addListener(future -> {
            if(future.isSuccess()){
                log.debug("Success to create connection with chat-server.");
                Channel channel = ((ChannelFuture) future).channel();
                startCmdThread(channel);
            } else {
                log.debug("Failed to create connection with chat-server.");
            }
        }).sync().channel();
    }
    private static void startCmdThread(Channel channel) {
        Scanner sc = new Scanner(System.in);
        InitialCmdConsole cmdConsole = new InitialCmdConsole();
        System.out.println("Please enter your message:\n");
        new Thread(
                ()-> {
                    while (!Thread.interrupted()) {
                        try {
                            cmdConsole.exec(sc, channel);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
        ).start();
    }
}
