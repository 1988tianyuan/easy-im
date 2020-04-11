package com.tianyuan.easyim.gateway.service.server;

import com.google.common.collect.Lists;
import com.tianyuan.easyim.gateway.config.properties.ZkProperties;
import com.tianyuan.easyim.gateway.model.Server;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Profile("!test")
public class ZkServerCollector implements ServerCollector {

    @Autowired
    private ZkProperties properties;

    private CuratorFramework client;

    @PostConstruct
    public void init() {
        client = CuratorFrameworkFactory.newClient(properties.getConnectString(), new RetryForever(1000));
        client.start();
    }

    @PreDestroy
    public void clear() {
        client.close();
    }

    @Override
    public List<Server> getServers() {
        try {
            List<Server> servers = Lists.newArrayList();
            List<String> serverIdList = client.getChildren().forPath(properties.getServerListPath());
            for (String serverId : serverIdList) {
                String address = new String(client.getData().forPath(properties.getServerListPath() + "/" + serverId),
                        StandardCharsets.UTF_8);
                servers.add(new Server(serverId, address));
            }
            return servers;
        } catch (Exception e) {
            throw new RuntimeException("Get servers from ZK fail", e);
        }
    }
}
