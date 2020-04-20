package com.tianyuan.easyim.chatserver.server;

import com.tianyuan.easyim.chatserver.config.ChatServerConfigs;
import com.tianyuan.easyim.chatserver.handler.ChatServerInitHandler;
import com.tianyuan.easyim.chatserver.handler.ChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.tianyuan.easyim.common.model.CommonConstant.SERVER_ID;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/13 17:30
 */
@Slf4j
public class NettyServer {
	
	private final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
	
	private final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private final String serverId;
	
	private final String host;
	
	private final int port;
	
	private final ChatServerConfigs configs;
	
	private List<Runnable> startHooks = new ArrayList<>();
	
	private List<Runnable> shutdownHooks = new ArrayList<>();
	
	public NettyServer(String serverId, ChatServerConfigs configs) {
		this.serverId = serverId;
		host = configs.getServer().getHost();
		port = configs.getServer().getPort();
		this.configs = configs;
	}
	
	public ChannelFuture start() {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new ChatServerInitializer())
			.childHandler(new ChatServerInitHandler(configs))
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childAttr(AttributeKey.newInstance(SERVER_ID), serverId)
			.option(ChannelOption.SO_BACKLOG, 1024);
		return serverBootstrap.bind(host, port).addListener(future -> {
			if (future.isSuccess()) {
				log.debug("Success to listen port:{}", port);
				startHooks.forEach(Runnable::run);
			} else {
				log.error("Fail to listen port:{}", port);
			}
		});
	}
	
	public void shutdown() {
		shutdownHooks.forEach(Runnable::run);
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}
	
	public void addStartHook(Runnable startHook) {
		startHooks.add(startHook);
	}
	
	public void addShutdownHook(Runnable shutdownHook) {
		shutdownHooks.add(shutdownHook);
	}
}
