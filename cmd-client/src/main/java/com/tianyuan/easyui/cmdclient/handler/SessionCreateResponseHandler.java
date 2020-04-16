package com.tianyuan.easyui.cmdclient.handler;

import static com.tianyuan.easyim.common.model.IMMsg.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/16 18:23
 */
public class SessionCreateResponseHandler extends SimpleChannelInboundHandler<SessionCreateResponseMsg> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SessionCreateResponseMsg msg) throws Exception {
		msg.get
	}
}
