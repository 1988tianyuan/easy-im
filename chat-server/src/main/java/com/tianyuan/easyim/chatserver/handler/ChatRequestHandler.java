package com.tianyuan.easyim.chatserver.handler;

import static com.tianyuan.easyim.common.model.IMMsg.*;

import com.tianyuan.easyim.common.model.IMMsg;
import com.tianyuan.easyim.common.protocal.ChatMsgUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@ChannelHandler.Sharable
@Slf4j
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
    
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Success to create connection with remote address:{}, channelId is {}",
            ctx.channel().remoteAddress(), ctx.channel().id());
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Finish the connection with remote address:{}, channelId is {}",
            ctx.channel().remoteAddress(), ctx.channel().id());
    }

    public static final ChatRequestHandler INSTANCE = new ChatRequestHandler();
}
