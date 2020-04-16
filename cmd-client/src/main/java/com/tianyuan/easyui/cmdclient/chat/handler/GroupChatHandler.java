package com.tianyuan.easyui.cmdclient.chat.handler;

import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.chat.ChatType;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/16 11:15
 */
public class GroupChatHandler extends ChatHandler {
	
	public GroupChatHandler(ChatContext chatContext) {
		super(chatContext, ChatType.GROUP);
	}
	
	@Override
	public void execMsg(String msg) {
		
	}
	
	@Override
	public String parseTarget(String input) {
		return parseGroup(input, 1);
	}
	
	@Override
	public String parseMessage(String input) {
		return parseGroup(input, 2);
	}
}
