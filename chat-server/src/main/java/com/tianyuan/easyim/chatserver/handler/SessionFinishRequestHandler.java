package com.tianyuan.easyim.chatserver.handler;

import static com.tianyuan.easyim.chatserver.session.SessionManagerHolder.*;
import static com.tianyuan.easyim.common.model.IMMsg.*;

import com.tianyuan.easyim.chatserver.config.ChatServerConfigs;
import com.tianyuan.easyim.chatserver.session.Session;
import com.tianyuan.easyim.chatserver.session.channel.GlobalChannelHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/22 17:59
 */
public class SessionFinishRequestHandler extends SimpleChannelInboundHandler<FinishSessionRequestMsg> {
	
	private ChatServerConfigs configs;
	
	public SessionFinishRequestHandler(ChatServerConfigs configs) {
		this.configs = configs;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FinishSessionRequestMsg msg) throws Exception {
		Session session = getSessionManager(configs).removeSession(msg.getSessionId());
		if (session != null) {
			GlobalChannelHolder.removeChannel(session.getUsername());
		}
	}
}
