package com.tianyuan.easyui.cmdclient.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.tianyuan.easyim.common.model.IMMsg.ChatResponseMsg;

@ChannelHandler.Sharable
public class ChatClientResponseHandler extends SimpleChannelInboundHandler<ChatResponseMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatResponseMsg msg) throws Exception {
        System.out.println("Received response msg from server: " + msg.getMessage());
        // TODO: handle response
    }

    public static final ChatClientResponseHandler INSTANCE = new ChatClientResponseHandler();
}
