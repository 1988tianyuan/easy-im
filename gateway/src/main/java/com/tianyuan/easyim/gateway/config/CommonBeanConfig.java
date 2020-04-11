package com.tianyuan.easyim.gateway.config;

import com.tianyuan.easyim.common.token.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
public class CommonBeanConfig {

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public TokenGenerator tokenGenerator(@Value("${token.key-path}") String keyPath) throws IOException {
        return new TokenGenerator(resourceLoader.getResource(keyPath).getFile());
    }
}
