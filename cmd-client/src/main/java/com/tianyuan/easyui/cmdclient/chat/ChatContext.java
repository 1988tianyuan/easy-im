package com.tianyuan.easyui.cmdclient.chat;

import io.netty.channel.Channel;
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
	
	private ChatServerConnector chatServerConnector;

	private volatile ClientStatus status = ClientStatus.INIT;
	
	private volatile String username;

	public void shutdown() {
		if (chatChannel != null && chatChannel.isOpen()) {
			chatChannel.close();
		}
		chatServerConnector.shutdown();
		System.out.println("Bye bye!");
	}

	public boolean quited() {
		return ClientStatus.QUIT.equals(status);
	}
}
