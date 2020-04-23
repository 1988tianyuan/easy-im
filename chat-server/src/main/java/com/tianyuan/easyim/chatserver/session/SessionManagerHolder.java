package com.tianyuan.easyim.chatserver.session;

import com.tianyuan.easyim.chatserver.config.ChatServerConfigs;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/23 14:52
 */
public class SessionManagerHolder {
	
	private static SessionManager sessionManager;
	
	public static SessionManager getSessionManager(ChatServerConfigs configs) {
		// TODO: use more sessionManager implemention by config
		// TODO: use global bean context(like Google-Guice) to manage the singleton
		if (sessionManager == null) {
			synchronized (SessionManagerHolder.class) {
				if (sessionManager == null) {
					sessionManager = new DefaultInMemorySessionManager(configs.getServer().getServerId());
				}
			}
		}
		return sessionManager;
	}
}
