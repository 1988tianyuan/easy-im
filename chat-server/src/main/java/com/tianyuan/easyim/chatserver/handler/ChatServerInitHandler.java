package com.tianyuan.easyim.chatserver.handler;

import com.tianyuan.easyim.common.protocal.ChatDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServerInitHandler extends ChannelInitializer<NioSocketChannel> {

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
        // chat-server chat request handler
        pipeline.addLast(ChatRequestHandler.INSTANCE);
    }
}