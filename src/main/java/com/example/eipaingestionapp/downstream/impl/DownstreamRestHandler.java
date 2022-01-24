package com.example.eipaingestionapp.downstream.impl;

import com.example.eipaingestionapp.downstream.api.Downstream;
import com.example.eipaingestionapp.downstream.api.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class DownstreamRestHandler<T> implements Downstream<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownstreamRestHandler.class);

    private final WebClient webClient;

    public DownstreamRestHandler(@Qualifier("downstreamClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void sendEvents(List<Event<T>> events) {
        webClient.post()
                .body(Mono.just(events), List.class)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> {
                            LOGGER.warn(String.format("Posting events ends with status %s", clientResponse.statusCode()));
                            return Mono.empty();
                        }
                )
                .bodyToMono(Void.class)
                .subscribe();
    }
}
