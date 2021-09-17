package com.example.eipaingestionapp.pooling;

import com.example.eipaingestionapp.downstream.api.Downstream;
import com.example.eipaingestionapp.pooling.model.EventPointStatus;
import com.example.eipaingestionapp.pooling.model.PointInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class PoolingHandler {
    private final Map<String, PointInfo> actualPoints = new ConcurrentHashMap<>();
    private final WebClient webClient;
    private final Downstream<EventPointStatus> downstream;

    public PoolingHandler(@Qualifier("poolingClient") WebClient webClient, Downstream<EventPointStatus> downstream) {
        this.webClient = webClient;
        this.downstream = downstream;
    }

    @PostConstruct
    private void init() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        DynamicEipaEndpointPoller poller = new DynamicEipaEndpointPoller(webClient, actualPoints, downstream);
        executorService.scheduleAtFixedRate(poller, 0, 15, TimeUnit.SECONDS);
    }
}
