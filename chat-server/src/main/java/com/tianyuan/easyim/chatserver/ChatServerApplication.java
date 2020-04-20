package com.tianyuan.easyim.chatserver;

import static com.tianyuan.easyim.chatserver.config.ConfigUtil.*;

import java.util.UUID;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryForever;

import com.google.common.base.Preconditions;
import com.tianyuan.easyim.chatserver.config.ChatServerConfigs;
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
	
	public static void main(String[] args) throws Exception {
		ChatServerConfigs configs = loadConfigs(args);
		// TODO: configurable
		String serverId = UUID.randomUUID().toString();
		NettyServer nettyServer = makeNettyServer(serverId, configs);
		ChannelFuture channelFuture = nettyServer.start();
		shutdownHook(nettyServer, channelFuture);
		channelFuture.sync();
	}
	
	private static NettyServer makeNettyServer(String serverId, ChatServerConfigs configs) {
		// use zk register
		String zkConnectStr = configs.getRegister().getZkConnectString();
		Preconditions.checkNotNull(zkConnectStr, "register.zkConnectString should not be null when use zk register.");
		CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zkConnectStr, new RetryForever(1000));
		ServerRegister register = new ZkRegister(zkClient);
		Runnable zkStartHook = makeZkStartHook(zkClient, register, serverId, configs);
		Runnable zkShutdownHook = makeZkShutdownHook(zkClient, register, serverId);
		NettyServer nettyServer = new NettyServer(serverId, configs);
		nettyServer.addStartHook(zkStartHook);
		nettyServer.addShutdownHook(zkShutdownHook);
		return nettyServer;
	}
	
	private static Runnable makeZkStartHook(CuratorFramework zkClient, ServerRegister register, 
		String serverId, ChatServerConfigs configs) {
		return () -> {
			zkClient.start();
			register.register(configs.getRegister().getAdvertiseHost(), configs.getServer().getPort(), serverId);
		};
	}
	
	private static Runnable makeZkShutdownHook(CuratorFramework zkClient, ServerRegister register, String serverId) {
		return () -> {
			register.deregister(serverId);
			if (CuratorFrameworkState.STARTED.equals(zkClient.getState())) {
				zkClient.close();
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
