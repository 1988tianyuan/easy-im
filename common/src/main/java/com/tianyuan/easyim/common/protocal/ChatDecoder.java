package com.tianyuan.easyim.common.protocal;

import com.tianyuan.easyim.common.model.IMMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

@ChannelHandler.Sharable
public class ChatDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] array = ByteBufUtil.getBytes(msg, msg.readerIndex(), msg.readableBytes(), false);
        IMMsg.BaseRequestMsg baseRequestMsg = IMMsg.BaseRequestMsg.parseFrom(array);
        out.add(ChatMsgUtil.parseRequestMsg(baseRequestMsg));
    }

    public static final ChatDecoder INSTANCE = new ChatDecoder();
}
