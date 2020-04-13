package com.tianyuan.easyim.chatserver;

import java.util.UUID;

import com.tianyuan.easyim.chatserver.server.NettyServer;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/10 16:34
 */
@Slf4j
public class ChatServerApplication {
	
	public static void main(String[] args) throws InterruptedException {
		// TODO: configurable
		NettyServer nettyServer = new NettyServer(UUID.randomUUID().toString());
		ChannelFuture channelFuture = nettyServer.start(8000);
		shutdownHook(nettyServer, channelFuture);
		channelFuture.sync();
	}
	
	private static void shutdownHook(NettyServer nettyServer, ChannelFuture channelFuture) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				channelFuture.channel().close().sync();
			} catch (InterruptedException e) {
				// nothing;
			}
			nettyServer.shutdown();
		}));
	}
}
