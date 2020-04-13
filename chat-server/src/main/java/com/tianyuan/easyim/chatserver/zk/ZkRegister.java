package com.tianyuan.easyim.chatserver.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import com.tianyuan.easyim.chatserver.exception.ChatServerException;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/13 17:28
 */
public class ZkRegister {
	
	private final CuratorFramework zkClient;
	
	// TODO: configurable
	private final String serverListPath = "/easy-im/chat-servers";
	
	public ZkRegister(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}
	
	public void registerToZk(String serverHost, int serverPort, String serverId) {
		String registerPath = makePath(serverId);
		try {
			zkClient.create()
				.creatingParentsIfNeeded()
				.withMode(CreateMode.EPHEMERAL)
				.forPath(registerPath, (serverHost + ":" + serverPort).getBytes());
		} catch (Exception e) {
			throw new ChatServerException("Fail to register current chat-server to path:" + registerPath, e);
		}
	}
	
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
