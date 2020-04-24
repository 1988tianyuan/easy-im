package com.tianyuan.easyui.cmdclient.console;

import static com.tianyuan.easyim.common.model.IMMsg.*;

import java.util.Scanner;

import com.tianyuan.easyim.common.protocal.ChatMsgUtil;
import com.tianyuan.easyui.cmdclient.chat.ChatContext;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/24 17:36
 */
public class GetUserListConsole implements CmdConsole {
	
	private final ChatContext chatContext;
	
	public GetUserListConsole(ChatContext chatContext) {
		this.chatContext = chatContext;
	}
	
	@Override
	public void exec(Scanner scanner) {
		GetOnlineUsersRequestMsg request = GetOnlineUsersRequestMsg.newBuilder()
			.setSessionId(chatContext.getSessionId()).build();
		BaseRequestMsg baseRequest = ChatMsgUtil.createBaseMsg(RequestType.GetOnlineUsers, request);
		chatContext.getChatChannel().writeAndFlush(baseRequest).addListener(future -> {
				if (!future.isSuccess()) {
					System.out.println("Failed to get online users, please try again.");
				}
			});
	}
}
