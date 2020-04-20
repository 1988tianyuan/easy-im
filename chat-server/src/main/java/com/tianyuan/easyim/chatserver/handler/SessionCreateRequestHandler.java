package com.tianyuan.easyim.chatserver.handler;

import com.tianyuan.easyim.chatserver.config.ChatServerConfigs;
import com.tianyuan.easyim.common.exception.TokenVerificationException;
import com.tianyuan.easyim.common.model.TokenData;
import com.tianyuan.easyim.common.protocal.ChatMsgUtil;
import com.tianyuan.easyim.common.token.TokenVerifier;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

import static com.tianyuan.easyim.common.model.IMMsg.*;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/16 18:23
 */
@Slf4j
public class SessionCreateRequestHandler extends SimpleChannelInboundHandler<SessionCreateRequestMsg> {
	
	private TokenVerifier tokenVerifier;
	
	public SessionCreateRequestHandler(ChatServerConfigs configs) throws Exception {
		String keyPath = configs.getToken().getPublicKeyPath();
		File file = new File(keyPath);
		if (!file.exists()) {
			throw new FileNotFoundException("publicKeyPath:"+ keyPath +" is not right, can't find publicKey file!");
		}
		tokenVerifier = new TokenVerifier(file);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SessionCreateRequestMsg msg) throws Exception {
		String jwt = msg.getJwt();
		SessionCreateResponseMsg.Builder builder = SessionCreateResponseMsg.newBuilder();
		try {
			tokenVerifier.verifyAndGetData(jwt, TokenData.class);
			builder.setSuccess(true);
			// TODO: create session and save into Redis
			builder.setSessionId(UUID.randomUUID().toString());
		} catch (TokenVerificationException e) {
			builder.setSuccess(false);
		}
		SessionCreateResponseMsg responseMsg = builder.build();
		ctx.writeAndFlush(ChatMsgUtil.createBaseMsg(RequestType.CreateSessionResponse, responseMsg));
	}
}
