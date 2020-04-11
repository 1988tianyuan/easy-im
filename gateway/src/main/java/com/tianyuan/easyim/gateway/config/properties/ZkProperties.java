package com.tianyuan.easyim.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "zk")
@Component
@Data
public class ZkProperties {

    private String connectString;

    private String serverListPath;
}
