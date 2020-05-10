package com.tianyuan.easyim.chatserver.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/22 17:42
 */
public class DefaultInMemorySessionManager implements SessionManager {
	
	private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
	
	private String serverId;
	
	public DefaultInMemorySessionManager(String serverId) {
		this.serverId = serverId;
	}
	
	@Override
	public Session removeSession(String sessionId) {
		return sessionMap.remove(sessionId);
	}
	
	@Override
	public Session createSession(String username) {
		Session newSession = createNewSession(username);
		sessionMap.put(newSession.getSessionId(), newSession);
		return newSession;
	}
	
	@Override
	public Session findSession(String sessionId) {
		return sessionMap.get(sessionId);
	}
	
	@Override
	public void clearAllSessions() {
		sessionMap.clear();
	}
	
	@Override
	public List<Session> getAllSessions() {
		return new ArrayList<>(sessionMap.values());
	}
	
	private Session createNewSession(String username) {
		String sessionId = UUID.randomUUID().toString();
		return Session.builder()
			.username(username)
			.createdTime(System.currentTimeMillis())
			.serverId(serverId)
			.sessionId(sessionId)
			.build();
	}
}
