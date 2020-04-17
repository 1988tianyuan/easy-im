package com.tianyuan.easyui.cmdclient.handler;

import com.tianyuan.easyim.common.protocal.ChatDecoder;
import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ChatClientInitializer extends ChannelInitializer<NioSocketChannel> {
    
    private final ChatContext chatContext;
    
    public ChatClientInitializer(ChatContext chatContext) {
        this.chatContext = chatContext;
    }
    
    @Override
    protected void initChannel(NioSocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        // protobuf tcp package spliter
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        // protobuf tcp package decoder
        pipeline.addLast(ChatDecoder.INSTANCE);
        // protobuf tcp package spliter
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        // protobuf tcp package encoder
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(new SessionCreateResponseHandler(chatContext));
        pipeline.addLast(ChatClientResponseHandler.INSTANCE);
    }
}
