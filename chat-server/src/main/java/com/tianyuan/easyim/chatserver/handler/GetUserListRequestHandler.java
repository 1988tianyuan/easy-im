package com.tianyuan.easyim.chatserver.handler;

import static com.tianyuan.easyim.chatserver.session.SessionManagerHolder.*;
import static com.tianyuan.easyim.common.model.IMMsg.*;

import java.util.List;
import java.util.stream.Collectors;

import com.tianyuan.easyim.chatserver.config.ChatServerConfigs;
import com.tianyuan.easyim.chatserver.session.Session;
import com.tianyuan.easyim.common.protocal.ChatMsgUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@ChannelHandler.Sharable
@Slf4j
public class GetUserListRequestHandler extends SimpleChannelInboundHandler<GetOnlineUsersRequestMsg> {
    
    private ChatServerConfigs configs;
    
    public GetUserListRequestHandler(ChatServerConfigs configs) {
        this.configs = configs;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GetOnlineUsersRequestMsg msg) throws Exception {
        List<Session> sessionList = getSessionManager(configs).getAllSessions();
        List<UserMsg> userList = sessionList.stream()
            .map(session -> UserMsg.newBuilder().setUsername(session.getUsername()).build())
            .collect(Collectors.toList());
        GetOnlineUsersResponseMsg responseMsg = GetOnlineUsersResponseMsg.newBuilder()
            .addAllUser(userList)
            .build();
        ctx.writeAndFlush(ChatMsgUtil.createBaseMsg(RequestType.OnlineUsersResponse, responseMsg));
    }
}
