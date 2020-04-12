package com.tianyuan.easyui.cmdclient;

import com.tianyuan.easyim.common.model.LoginResponse;
import com.tianyuan.easyui.cmdclient.chat.ChatServerConnector;
import com.tianyuan.easyui.cmdclient.login.LoginHandler;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/10 16:36
 */
@Slf4j
public class CmdClientApplication {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Please login firstly, enter your username: ");
		Scanner scanner = new Scanner(System.in);
		String username = scanner.next();
		LoginResponse loginResponse = LoginHandler.login(username);
		ChatServerConnector connector = new ChatServerConnector();
		Channel channel = connector.start(loginResponse.getServerAddress());
		//TODO: send CreateConnectRequest to server with JWT
	}
}
