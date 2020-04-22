package com.tianyuan.easyui.cmdclient.chat;

import static com.tianyuan.easyui.cmdclient.chat.ClientStatus.*;

import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/13 11:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatContext {
	
	private Channel chatChannel;
	
	private NioEventLoopGroup eventLoopGroup;

	private volatile ClientStatus status;
	
	private volatile String username;
	
	private String sessionId;

	public void shutdown() {
		if (chatChannel != null && chatChannel.isOpen()) {
			chatChannel.close();
		}
		eventLoopGroup.shutdownGracefully();
		System.out.println("Bye bye!");
	}

	public boolean quited() {
		return ClientStatus.QUITTED.equals(status);
	}
	
	public void init() {
		// TODO: configurable
		this.eventLoopGroup = new NioEventLoopGroup();
		this.status = INIT;
	}
	
	public void logoutClear() {
		sessionId = null;
		username = null;
		chatChannel.close();	//TODO: use connection pool to re-use channel
		chatChannel = null;
		status = ClientStatus.INIT;
	}
}
