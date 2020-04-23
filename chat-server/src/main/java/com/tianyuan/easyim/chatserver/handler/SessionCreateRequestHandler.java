package com.tianyuan.easyim.chatserver.handler;

import static com.tianyuan.easyim.chatserver.session.SessionManagerHolder.*;
import static com.tianyuan.easyim.common.model.IMMsg.*;

import java.io.File;
import java.io.FileNotFoundException;

import com.tianyuan.easyim.chatserver.config.ChatServerConfigs;
import com.tianyuan.easyim.chatserver.session.Session;
import com.tianyuan.easyim.chatserver.session.channel.GlobalChannelHolder;
import com.tianyuan.easyim.common.exception.TokenVerificationException;
import com.tianyuan.easyim.common.model.TokenData;
import com.tianyuan.easyim.common.protocal.ChatMsgUtil;
import com.tianyuan.easyim.common.token.TokenVerifier;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/16 18:23
 */
@Slf4j
public class SessionCreateRequestHandler extends SimpleChannelInboundHandler<SessionCreateRequestMsg> {
	
	private TokenVerifier tokenVerifier;
	
	private ChatServerConfigs configs;
	
	public SessionCreateRequestHandler(ChatServerConfigs configs) throws Exception {
		String keyPath = configs.getToken().getPublicKeyPath();
		File file = new File(keyPath);
		if (!file.exists()) {
			throw new FileNotFoundException("publicKeyPath:"+ keyPath +" is not right, can't find publicKey file!");
		}
		this.tokenVerifier = new TokenVerifier(file);
		this.configs = configs;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SessionCreateRequestMsg msg) throws Exception {
		String jwt = msg.getJwt();
		String username = msg.getUsername();
		SessionCreateResponseMsg.Builder builder = SessionCreateResponseMsg.newBuilder();
		try {
			tokenVerifier.verifyAndGetData(jwt, TokenData.class);
			builder.setSuccess(true);
			Session session = getSessionManager(configs).createSession(username);
			saveSessionChannel(ctx.channel(), session.getSessionId());
			builder.setSessionId(session.getSessionId());
		} catch (TokenVerificationException e) {
			builder.setSuccess(false);
		}
		SessionCreateResponseMsg responseMsg = builder.build();
		ctx.writeAndFlush(ChatMsgUtil.createBaseMsg(RequestType.CreateSessionResponse, responseMsg));
	}
	
	private void saveSessionChannel(Channel channel, String sessionId) {
		GlobalChannelHolder.saveChannel(channel, sessionId, future -> getSessionManager(configs).removeSession(sessionId));
	}
}
