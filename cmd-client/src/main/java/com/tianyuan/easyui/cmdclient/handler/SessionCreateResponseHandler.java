package com.tianyuan.easyui.cmdclient.handler;

import static com.tianyuan.easyim.common.model.IMMsg.*;

import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.chat.ClientStatus;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/16 18:23
 */
@Slf4j
public class SessionCreateResponseHandler extends SimpleChannelInboundHandler<SessionCreateResponseMsg> {
	
	private final ChatContext chatContext;
	
	public SessionCreateResponseHandler(ChatContext chatContext) {
		this.chatContext = chatContext;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SessionCreateResponseMsg msg) throws Exception {
		if (msg.getSuccess()) {
			successLogin(msg.getSessionId());
		} else {
			failLogin(ctx.channel());
		}
	}
	
	private void successLogin(String sessionId) {
		chatContext.setSessionId(sessionId);
		chatContext.setStatus(ClientStatus.LOGGED_IN);
		log.debug("Success to login with username: {}", chatContext.getUsername());
		System.out.println("Success to login, enter use format ':targetUserName message' to begin a chat with targetUser");
	}
	
	private void failLogin(Channel currentChannel) {
		System.out.println("Failed to login, please try again.");
		currentChannel.close();
		chatContext.setChatChannel(null);
		chatContext.setStatus(ClientStatus.INIT);
	}
}
