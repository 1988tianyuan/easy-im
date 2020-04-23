package com.tianyuan.easyim.chatserver.register.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import com.tianyuan.easyim.chatserver.exception.ChatServerException;
import com.tianyuan.easyim.chatserver.register.ServerRegister;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/13 17:28
 */
public class ZkRegister implements ServerRegister {
	
	// TODO: configurable
	private final String serverListPath = "/easy-im/chat-servers";
	
	private final CuratorFramework zkClient;
	
	public ZkRegister(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}
	
	@Override
	public void register(String serverHost, int serverPort, String serverId) {
		String registerPath = makePath(serverId);
		byte[] data = (serverHost + ":" + serverPort).getBytes();
		try {
			if (zkClient.checkExists().forPath(registerPath) == null) {
				zkClient.create()
					.creatingParentsIfNeeded()
					.withMode(CreateMode.EPHEMERAL)
					.forPath(registerPath, data);
			} else {
				zkClient.setData().forPath(registerPath, data);
			}
		} catch (Exception e) {
			throw new ChatServerException("Fail to register current chat-server to path:" + registerPath, e);
		}
	}
	
	@Override
	public void deregister(String serverId) {
		String registerPath = makePath(serverId);
		try {
			if (zkClient.checkExists().forPath(registerPath) != null) {
				zkClient.delete().forPath(registerPath);
			}
		} catch (Exception e) {
			throw new ChatServerException("Fail to deregister current chat-server");
		}
	}
	
	private String makePath(String serverId) {
		return serverListPath + "/" + serverId;
	}
}
