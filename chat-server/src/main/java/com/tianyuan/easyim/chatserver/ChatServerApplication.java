package com.tianyuan.easyim.chatserver;

import java.util.UUID;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryForever;

import com.tianyuan.easyim.chatserver.register.ServerRegister;
import com.tianyuan.easyim.chatserver.register.zk.ZkRegister;
import com.tianyuan.easyim.chatserver.server.NettyServer;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/10 16:34
 */
@Slf4j
public class ChatServerApplication {
	
	// TODO: configurable
	private static final String zkConnectString = "10.34.130.38:2181";
	
	public static void main(String[] args) throws InterruptedException {
		// TODO: configurable
		String serverId = UUID.randomUUID().toString();
		NettyServer nettyServer = makeNettyServer(serverId);
		ChannelFuture channelFuture = nettyServer.start(8000);
		shutdownHook(nettyServer, channelFuture);
		channelFuture.sync();
	}
	
	private static NettyServer makeNettyServer(String serverId) {
		// use zk register
		CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zkConnectString, new RetryForever(1000));
		ServerRegister register = new ZkRegister(zkClient);
		NettyServer.ServerHook zkHook = makeZkRegisterHook(zkClient);
		NettyServer nettyServer = new NettyServer(serverId, register);
		nettyServer.addServerHook(zkHook);
		return nettyServer;
	}
	
	private static NettyServer.ServerHook makeZkRegisterHook(CuratorFramework zkClient) {
		return new NettyServer.ServerHook() {
			@Override
			public void onStart() {
				zkClient.start();
			}
			
			@Override
			public void onShutdown() {
				if (CuratorFrameworkState.STARTED.equals(zkClient.getState())) {
					zkClient.close();
				}
			}
		};
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
