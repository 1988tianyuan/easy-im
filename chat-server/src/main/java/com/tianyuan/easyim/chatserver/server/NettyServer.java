package com.tianyuan.easyim.chatserver.server;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryForever;

import com.tianyuan.easyim.chatserver.handler.ChatServerInitHandler;
import com.tianyuan.easyim.chatserver.handler.ChatServerInitializer;
import com.tianyuan.easyim.chatserver.zk.ZkRegister;
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
	
	private final CuratorFramework zkClient;
	
	private final ZkRegister zkRegister;
	
	private final String serverId;
	
	// TODO: configurable
	private static final String zkConnectString = "localhost:2181";
	
	public NettyServer(String serverId) {
		this.zkClient = CuratorFrameworkFactory.newClient(zkConnectString, new RetryForever(1000));
		this.serverId = serverId;
		this.zkRegister = new ZkRegister(zkClient);
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
				zkClient.start();
				// TODO: configurable
				zkRegister.registerToZk("localhost", port, serverId);
			} else {
				log.error("Fail to listen port:{}", port);
				shutdown();
			}
		});
	}
	
	public void shutdown() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		zkRegister.deregister(serverId);
		if (CuratorFrameworkState.STARTED.equals(zkClient.getState())) {
			zkClient.close();
		}
	}
}
