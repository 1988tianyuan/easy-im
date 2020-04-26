package com.tianyuan.easyim.chatserver.session.channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/22 18:34
 */
public class GlobalChannelHolder {
	
	private static final Map<String, Channel> usernameChannelMap = new ConcurrentHashMap<>();
	
	public static void saveChannel(Channel channel, String sessionId, ChannelFutureListener closeListener) {
		usernameChannelMap.put(sessionId, channel);
		channel.closeFuture().addListeners(closeListener, future -> usernameChannelMap.remove(sessionId));
	}
	
	public static Channel removeChannel(String sessionId) {
		return usernameChannelMap.remove(sessionId);
	}
	
	public static Channel findChannel(String sessionId) {
		return usernameChannelMap.get(sessionId);
	}
}
