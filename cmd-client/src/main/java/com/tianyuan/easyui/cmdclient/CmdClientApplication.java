package com.tianyuan.easyui.cmdclient;

import com.tianyuan.easyui.cmdclient.console.InitialCmdConsole;
import com.tianyuan.easyui.cmdclient.handler.ChatClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/10 16:36
 */
@Slf4j
public class CmdClientApplication {

	// TODO: configurable
	public static final String host = "127.0.0.1";
	public static final int port = 8000;

	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap().group(workerGroup)
				.channel(NioSocketChannel.class)
				.handler(new ChatClientInitializer())
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
		connect(bootstrap);
	}

	private static void connect(Bootstrap bootstrap) throws InterruptedException {
		bootstrap.connect(host, port).addListener(future -> {
			if(future.isSuccess()){
				log.debug("Success to create connection with chat-server.");
				Channel channel = ((ChannelFuture) future).channel();
				startCmdThread(channel);
			} else {
				log.debug("Failed to create connection with chat-server.");
			}
		}).sync().channel().closeFuture().sync();
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
