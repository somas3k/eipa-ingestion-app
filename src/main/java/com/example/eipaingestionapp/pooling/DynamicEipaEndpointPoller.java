package com.example.eipaingestionapp.pooling;

import com.example.eipaingestionapp.downstream.api.Downstream;
import com.example.eipaingestionapp.downstream.api.model.Event;
import com.example.eipaingestionapp.pooling.model.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class DynamicEipaEndpointPoller implements Runnable {
    private final WebClient webClient;
    private final Map<String, PointInfo> actualPoints;
    private final Downstream<EventPointStatus> downstream;

    public DynamicEipaEndpointPoller(WebClient webClient, Map<String, PointInfo> actualPoints, Downstream<EventPointStatus> downstream) {
        this.webClient = webClient;
        this.actualPoints = actualPoints;
        this.downstream = downstream;
    }

    @Override
    public void run() {
        webClient.get()
                .retrieve()
                .bodyToMono(DynamicEndpointResponse.class)
                .subscribe(dynamicEndpointResponse -> sendToDownstream(dynamicEndpointResponse.getData().stream()
                        .filter(pointInfo -> Objects.nonNull(pointInfo.getStatus()))
                        .filter(this::isStatusChanged)
                        .map(this::convertToEvent)
                        .collect(Collectors.toList())));
    }

    private boolean isStatusChanged(PointInfo pointInfo) {
        PointInfo actualPointInfo = actualPoints.get(pointInfo.getCode());
        if (actualPointInfo == null || statusChanged(actualPointInfo, pointInfo)) {
            actualPoints.put(pointInfo.getCode(), pointInfo);
            return true;
        }
        return false;
    }

    private boolean statusChanged(PointInfo actualPointInfo, PointInfo pointInfo) {
        return actualPointInfo.getStatus().getStatus() != pointInfo.getStatus().getStatus() ||
                actualPointInfo.getStatus().getAvailability() != pointInfo.getStatus().getAvailability();
    }

    private void sendToDownstream(List<Event<EventPointStatus>> events) {
        downstream.sendEvents(events);
    }

    private Event<EventPointStatus> convertToEvent(PointInfo pointInfo) {
        return new Event<>(EventPointStatus.builder()
                .pointId(pointInfo.getCode())
                .status(calculateStatus(pointInfo.getStatus()))
                .originalTs(pointInfo.getStatus().getTimestamp())
                .build());
    }

    private Status calculateStatus(PointStatus status) {
        if (status.getAvailability() == 1 && status.getStatus() == 1) {
            return Status.AVAILABLE;
        }
        if (status.getAvailability() == 1 && status.getStatus() == 0) {
            return Status.OCCUPIED;
        }
        if (status.getAvailability() == 0) {
            return Status.OUT_OF_ORDER;
        }
        return Status.UNKNOWN;
    }
}
