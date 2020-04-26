package com.tianyuan.easyim.chatserver.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/22 17:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {
	
	private String sessionId;
	
	private String username;
	
	private String serverId;
	
	private long createdTime;
}
