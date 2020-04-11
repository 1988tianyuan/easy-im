package com.tianyuan.easyim.gateway.service.server;

import com.tianyuan.easyim.gateway.model.Server;
import com.tianyuan.easyim.gateway.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class ServerAssigner {

    private final ServerCollector collector;

    private Queue<Server> cache = new LinkedList<>();

    public Optional<Server> assign(User user) {
        if (cache.isEmpty()) {
            cache.addAll(collector.getServers());
        }

        return Optional.ofNullable(cache.poll());
    }
}
