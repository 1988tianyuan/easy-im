package com.tianyuan.easyim.chatserver.config;

import java.util.UUID;

import lombok.Data;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/17 16:10
 */
@Data
public class ChatServerConfigs {
	
	private ServerConfig server = new ServerConfig();
	
	private TokenConfig token = new TokenConfig();
	
	private RegisterConfig register = new RegisterConfig();
	
	@Data
	public static class ServerConfig {
		private int port = 8000;
		private String host = "0.0.0.0";
		private String serverId = UUID.randomUUID().toString();
		
		public void setPort(int port) {
			if (port != 0) {
				this.port = port;
			}
		}
		public void setServerId(String serverId) {
			if (serverId != null) {
				this.serverId = serverId;
			}
		}
	}
	
	@Data
	public static class TokenConfig {
		private String publicKeyPath = "rsa/public_key.der";
		
		public void setPublicKeyPath(String publicKeyPath) {
			if (publicKeyPath != null) {
				this.publicKeyPath = publicKeyPath;
			}
		}
	}
	
	@Data
	public static class RegisterConfig {
		private String zkConnectString;
		private String advertiseHost = "127.0.0.1";
		
		public void setAdvertiseHost(String advertiseHost) {
			if (advertiseHost != null) {
				this.advertiseHost = advertiseHost;
			}
		}
	}
	
	public void setServer(ServerConfig server) {
		if (server != null) {
			this.server = server;
		}
	}
	
	public void setToken(TokenConfig token) {
		if (token != null) {
			this.token = token;
		}
	}
	
	public void setRegister(RegisterConfig register) {
		if (register != null) {
			this.register = register;
		}
	}
}
