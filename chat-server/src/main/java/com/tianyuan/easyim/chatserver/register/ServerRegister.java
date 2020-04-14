package com.tianyuan.easyim.chatserver.register;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/14 13:21
 */
public interface ServerRegister {
	
	void register(String serverHost, int serverPort, String serverId);
	
	void deregister(String serverId);
}
