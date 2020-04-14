package com.tianyuan.easyim.chatserver.server;

import java.util.ArrayList;
import java.util.List;

import com.tianyuan.easyim.chatserver.handler.ChatServerInitHandler;
import com.tianyuan.easyim.chatserver.handler.ChatServerInitializer;
import com.tianyuan.easyim.chatserver.register.ServerRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/13 17:30
 */
@Slf4j
public class NettyServer {
	
	private final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
	
	private final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private final ServerRegister register;
	
	private final String serverId;
	
	private List<ServerHook> serverHooks = new ArrayList<>();
	
	public NettyServer(String serverId, ServerRegister register) {
		this.serverId = serverId;
		this.register = register;
	}
	
	public ChannelFuture start(int port) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new ChatServerInitializer())
			.childHandler(new ChatServerInitHandler())
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childOption(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_BACKLOG, 1024);
		return serverBootstrap.bind(port).addListener(future -> {
			if (future.isSuccess()) {
				log.debug("Success to listen port:{}", port);
				serverHooks.forEach(ServerHook::onStart);
				// TODO: configurable
				register.register("localhost", port, serverId);
			} else {
				log.error("Fail to listen port:{}", port);
			}
		});
	}
	
	public void shutdown() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		register.deregister(serverId);
		serverHooks.forEach(ServerHook::onShutdown);
	}
	
	public void addServerHook(ServerHook serverHook) {
		serverHooks.add(serverHook);
	}
	
	public interface ServerHook {
		void onStart();
		
		void onShutdown();
	}
}
