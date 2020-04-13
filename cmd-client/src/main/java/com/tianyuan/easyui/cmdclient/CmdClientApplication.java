package com.tianyuan.easyui.cmdclient;

import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.chat.ChatServerConnector;
import com.tianyuan.easyui.cmdclient.console.CommandConsoleManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/10 16:36
 */
@Slf4j
public class CmdClientApplication {
	
	private static final ChatContext chatContext = new ChatContext();

	public static void main(String[] args) {
		ChatServerConnector chatServerConnector = new ChatServerConnector();
		chatContext.setChatServerConnector(chatServerConnector);
		CommandConsoleManager consoleManager = new CommandConsoleManager(chatContext);
		consoleManager.cmdLoop();
		chatContext.shutdown();
	}
}
