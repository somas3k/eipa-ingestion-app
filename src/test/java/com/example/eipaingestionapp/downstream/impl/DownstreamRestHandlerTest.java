package com.example.eipaingestionapp.downstream.impl;

import com.example.eipaingestionapp.downstream.api.model.Event;
import com.example.eipaingestionapp.pooling.model.EventPointStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DownstreamRestHandlerTest {
    @Captor
    private ArgumentCaptor<Mono<List<Event<EventPointStatus>>>> captor;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    private DownstreamRestHandler<EventPointStatus> downstreamRestHandler;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(captor.capture(), (Class<List<Event<EventPointStatus>>>) any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());
        downstreamRestHandler = new DownstreamRestHandler<>(webClient);
    }

    @Test
    void shouldPostEvents() {
        //given
        List<Event<EventPointStatus>> events = new ArrayList<>();
        Event<EventPointStatus> event = new Event<>();
        event.setData(new EventPointStatus());
        events.add(event);

        //when
        downstreamRestHandler.sendEvents(events);

        //then
        List<Event<EventPointStatus>> capturedEvents = captor.getValue().block();
        assertEquals(events, capturedEvents);
    }
}
