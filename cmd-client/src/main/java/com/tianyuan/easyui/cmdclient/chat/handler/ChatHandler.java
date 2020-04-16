package com.tianyuan.easyui.cmdclient.chat.handler;

import java.util.regex.Matcher;

import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.chat.ChatType;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/16 10:59
 */
public abstract class ChatHandler {
	
	protected final ChatContext chatContext;
	
	protected final ChatType chatType;
	
	protected ChatHandler(ChatContext chatContext, ChatType chatType) {
		this.chatContext = chatContext;
		this.chatType = chatType;
	}
	
	public abstract void execMsg(String input);
	
	public abstract String parseTarget(String input);
	
	public abstract String parseMessage(String input);
	
	protected String parseChatElement(String input, int index) {
		Matcher matcher = chatType.getMsgPattern().matcher(input);
		if (matcher.find()) {
			return matcher.group(index);
		}
		return null;
	}
}
