package com.example.eipaingestionapp.pooling;

import com.example.eipaingestionapp.downstream.api.Downstream;
import com.example.eipaingestionapp.downstream.api.model.Event;
import com.example.eipaingestionapp.pooling.model.DynamicEndpointResponse;
import com.example.eipaingestionapp.pooling.model.EventPointStatus;
import com.example.eipaingestionapp.pooling.model.PointInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class DynamicEipaEndpointPoolingWorkerTest {
    @Captor
    private ArgumentCaptor<List<Event<EventPointStatus>>> captor;
    private DynamicEipaEndpointPoolingWorker worker;
    @Mock
    private WebClient webClient;
    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;
    @Mock
    private Downstream<EventPointStatus> downstream;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
    }

    @Test
    void shouldHandlePointInfosAndSendToDownstreamWhenNoActualPoints() {
        //given
        worker = new DynamicEipaEndpointPoolingWorker(webClient, new HashMap<>(), downstream);
        DynamicEndpointResponse data = new DynamicEndpointResponse();
        data.setData(Arrays.asList(
                TestUtils.getPointInfo("123456", 1, 1, "2022-01-24T22:27:20+01:00"),
                TestUtils.getPointInfo("654321", 1, 1, "2022-01-24T22:27:20+01:00")
        ));
        when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<DynamicEndpointResponse>>notNull())).thenReturn(Mono.just(data));

        //when
        worker.run();

        //then
        verify(downstream, times(1)).sendEvents(captor.capture());
        List<Event<EventPointStatus>> events = captor.getValue();
        assertEquals(2, events.size());
    }

    @Test
    void shouldHandlePointInfosAndSendToDownstreamWhenIsActualPointWithTheSameStatusAsNewOne() {
        //given
        PointInfo pointInfo = TestUtils.getPointInfo("123456", 1, 1, "2022-01-24T22:27:20+01:00");
        Map<String, PointInfo> actualPoints = new HashMap<>();
        actualPoints.put("123456", pointInfo);
        worker = new DynamicEipaEndpointPoolingWorker(webClient, actualPoints, downstream);
        DynamicEndpointResponse data = new DynamicEndpointResponse();
        data.setData(Arrays.asList(
                pointInfo,
                TestUtils.getPointInfo("654321", 1, 1, "2022-01-24T22:27:20+01:00")
        ));
        when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<DynamicEndpointResponse>>notNull())).thenReturn(Mono.just(data));

        //when
        worker.run();

        //then
        verify(downstream, times(1)).sendEvents(captor.capture());
        List<Event<EventPointStatus>> events = captor.getValue();
        assertEquals(1, events.size());
    }

    @Test
    void shouldHandlePointInfosAndSendToDownstreamWhenIsActualPointWithNotTheSameStatusAsNewOne() {
        //given
        PointInfo pointInfoOld = TestUtils.getPointInfo("123456", 1, 1, "2022-01-24T22:27:20+01:00");
        Map<String, PointInfo> actualPoints = new HashMap<>();
        actualPoints.put("123456", pointInfoOld);
        worker = new DynamicEipaEndpointPoolingWorker(webClient, actualPoints, downstream);
        DynamicEndpointResponse data = new DynamicEndpointResponse();
        data.setData(Arrays.asList(
                TestUtils.getPointInfo("123456", 1, 0, "2022-01-24T22:27:20+01:00"),
                TestUtils.getPointInfo("654321", 1, 1, "2022-01-24T22:27:20+01:00")
        ));
        when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<DynamicEndpointResponse>>notNull())).thenReturn(Mono.just(data));

        //when
        worker.run();

        //then
        verify(downstream, times(1)).sendEvents(captor.capture());
        List<Event<EventPointStatus>> events = captor.getValue();
        assertEquals(2, events.size());
    }
}
