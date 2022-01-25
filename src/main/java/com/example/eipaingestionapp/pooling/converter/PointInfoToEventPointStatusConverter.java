package com.example.eipaingestionapp.pooling.converter;

import com.example.eipaingestionapp.downstream.api.model.Event;
import com.example.eipaingestionapp.pooling.model.EventPointStatus;
import com.example.eipaingestionapp.pooling.model.PointInfo;
import com.example.eipaingestionapp.pooling.model.PointStatus;
import com.example.eipaingestionapp.pooling.model.Status;

public class PointInfoToEventPointStatusConverter {

    private PointInfoToEventPointStatusConverter() {
        //hide implicit constructor
    }

    public static Event<EventPointStatus> convertToEvent(PointInfo pointInfo) {
        return new Event<>(EventPointStatus.builder()
                .pointId(pointInfo.getCode())
                .status(calculateStatus(pointInfo.getStatus()))
                .originalTs(pointInfo.getStatus().getTimestamp())
                .build());
    }

    private static Status calculateStatus(PointStatus status) {
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
