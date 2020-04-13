package com.tianyuan.easyim.chatserver.exception;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/13 17:32
 */
public class ChatServerException extends RuntimeException {
	
	public ChatServerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ChatServerException(String message) {
		super(message);
	}
}
