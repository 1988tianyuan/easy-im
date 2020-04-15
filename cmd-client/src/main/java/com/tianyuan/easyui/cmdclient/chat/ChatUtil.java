package com.tianyuan.easyui.cmdclient.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/15 10:20
 */
public class ChatUtil {
	
	private static final Pattern chatMsgPattern = Pattern.compile("^:(\\S*)\\s+(.*)$");
	
	public static boolean isValidChat(String input) {
		if (input.startsWith(":")) {
			if (chatMsgPattern.matcher(input).matches()) {
				return true;
			}
			System.out.println("If you want to chat, please input valid format like this: ':targetUserName message'");
		}
		return false;
	}
	
	public static String parseTargetUer(String input) {
		return parseGroup(input, 1);
	}
	
	public static String parseChatMsg(String input) {
		return parseGroup(input, 2);
	}
	
	private static String parseGroup(String input, int groupIndex) {
		Matcher matcher = chatMsgPattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(groupIndex);
		}
		return null;
	}
}
