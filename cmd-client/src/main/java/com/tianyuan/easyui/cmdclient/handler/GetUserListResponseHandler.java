package com.tianyuan.easyui.cmdclient.handler;

import static com.tianyuan.easyim.common.model.IMMsg.*;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/27 10:03
 */
public class GetUserListResponseHandler extends SimpleChannelInboundHandler<GetOnlineUsersResponseMsg> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, GetOnlineUsersResponseMsg msg) throws Exception {
		List<UserMsg> userList = msg.getUserList();
		System.out.println("all the online users: ");
		userList.forEach(user -> System.out.print(user.getUsername() + " | "));
	}
}
