package com.example.eipaingestionapp.pooling.converter;

import com.example.eipaingestionapp.downstream.api.model.Event;
import com.example.eipaingestionapp.pooling.TestUtils;
import com.example.eipaingestionapp.pooling.model.EventPointStatus;
import com.example.eipaingestionapp.pooling.model.PointInfo;
import com.example.eipaingestionapp.pooling.model.PointStatus;
import com.example.eipaingestionapp.pooling.model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointInfoToEventPointStatusConverterTest {
    private static final String CODE = "123456";
    private static final String TIMESTAMP = "2022-01-24T22:27:20+01:00";

    @Test
    void shouldConvertPointInfoToEventPointStatus() {
        //given
        PointInfo pointInfo1 = getPointInfo(1, 1);
        PointInfo pointInfo2 = getPointInfo(0, 1);
        PointInfo pointInfo3 = getPointInfo(0, 0);
        PointInfo pointInfo4 = getPointInfo(0, -1);

        //when
        Event<EventPointStatus> eventPointStatus1 = PointInfoToEventPointStatusConverter.convertToEvent(pointInfo1);
        Event<EventPointStatus> eventPointStatus2 = PointInfoToEventPointStatusConverter.convertToEvent(pointInfo2);
        Event<EventPointStatus> eventPointStatus3 = PointInfoToEventPointStatusConverter.convertToEvent(pointInfo3);
        Event<EventPointStatus> eventPointStatus4 = PointInfoToEventPointStatusConverter.convertToEvent(pointInfo4);

        //then
        assertEquals(CODE, eventPointStatus1.getData().getPointId());
        assertEquals(TIMESTAMP, eventPointStatus1.getData().getOriginalTs());

        assertEquals(Status.AVAILABLE, eventPointStatus1.getData().getStatus());
        assertEquals(Status.OCCUPIED, eventPointStatus2.getData().getStatus());
        assertEquals(Status.OUT_OF_ORDER, eventPointStatus3.getData().getStatus());
        assertEquals(Status.UNKNOWN, eventPointStatus4.getData().getStatus());
    }

    private PointInfo getPointInfo(int statusCode, int availability) {
        return TestUtils.getPointInfo(CODE, statusCode, availability, TIMESTAMP);
    }
}
