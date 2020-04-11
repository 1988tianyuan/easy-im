package com.tianyuan.easyim.server;

import com.tianyuan.easyim.server.handler.ChatServerInitHandler;
import com.tianyuan.easyim.server.handler.ChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/10 16:34
 */
@Slf4j
public class ChatServerApplication {

	//TODO: configurable
	private static final int port = 8000;
	
	public static void main(String[] args) throws InterruptedException {
		//BossSelector, accept new connections and assign to workerGroup
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		//WorkerSelector，manage connections with client
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new ChatServerInitializer())
					.childHandler(new ChatServerInitHandler())
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_BACKLOG, 1024);

			serverBootstrap.bind(port).addListener(future -> {
				if(future.isSuccess()){
					log.debug("端口{}绑定成功！", port);
				}else {
					log.error("端口{}绑定失败！", port);
				}
			}).sync().channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
