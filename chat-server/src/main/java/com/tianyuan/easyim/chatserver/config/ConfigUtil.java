package com.tianyuan.easyim.chatserver.config;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

import com.tianyuan.easyim.chatserver.ChatServerApplication;

/**
 * @author Liu Geng liu.geng@navercorp.com
 * @date 2020/4/17 16:48
 */
public class ConfigUtil {
	
	public static ChatServerConfigs loadConfigs(String[] args) throws Exception {
		// TODO: lookup config file location in args
		// use public_key.der by classpath
		return loadClasspathDefaultConfigFile();
	}
	
	public static ChatServerConfigs loadClasspathDefaultConfigFile() throws Exception {
		ClassLoader classLoader = ChatServerApplication.class.getClassLoader();
		try (InputStream inputStream = classLoader.getResourceAsStream("chat-server.yml")) {
			Yaml yaml = new Yaml();
			return yaml.loadAs(inputStream, ChatServerConfigs.class);
		}
	}
}
