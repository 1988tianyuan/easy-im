package com.tianyuan.easyim.chatserver.handler;

import com.tianyuan.easyim.common.model.IMMsg;
import com.tianyuan.easyim.common.protocal.ChatMsgUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.tianyuan.easyim.common.model.IMMsg.*;

@ChannelHandler.Sharable
public class ChatRequestHandler extends SimpleChannelInboundHandler<ChatRequestMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMsg msg) throws Exception {
        System.out.println("Receive msg from client: " + msg.getMessage());
        String responseStr = "I have received your msg: " + msg.getMessage();
        ChatResponseMsg responseMsg = ChatResponseMsg.newBuilder()
                .setMessage(responseStr)
                .setUsername(msg.getTargetUsername())
                .setTargetUsername(msg.getUsername())
                .build();
        ctx.writeAndFlush(ChatMsgUtil.createBaseMsg(IMMsg.RequestType.ChatResponse, responseMsg));
        // TODO: handle chat request message
    }

    public static final ChatRequestHandler INSTANCE = new ChatRequestHandler();
}
