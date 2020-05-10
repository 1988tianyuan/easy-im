package com.tianyuan.easyim.chatserver.session;

import java.util.List;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/22 17:27
 */
public interface SessionManager {
	
	Session removeSession(String sessionId);
	
	Session createSession(String username);
	
	Session findSession(String sessionId);
	
	void clearAllSessions();
	
	List<Session> getAllSessions();
}
