package com.tianyuan.easyui.cmdclient.chat;

import java.util.regex.Pattern;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/16 11:03
 */
public enum ChatType {
	// use format ":targetUser message", example: ":liugeng hello!"
	P2P(Pattern.compile("^:(\\S*)\\s+(.*)$")),
	
	// use format ">groupId message", example: ">group1 hello, group1 members!!"
	GROUP(Pattern.compile("^>(\\S*)\\s+(.*)$"));
	
	private Pattern msgPattern;
	
	public static ChatType getChatType(String input) {
		for (ChatType type : values()) {
			if (type.getMsgPattern().matcher(input).matches()) {
				return type;
			}
		}
		return null;
	}
	
	ChatType(Pattern msgPattern) {
		this.msgPattern = msgPattern;
	}
	
	public Pattern getMsgPattern() {
		return msgPattern;
	}
}
