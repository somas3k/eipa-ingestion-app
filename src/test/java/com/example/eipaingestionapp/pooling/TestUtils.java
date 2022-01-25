package com.example.eipaingestionapp.pooling;

import com.example.eipaingestionapp.pooling.model.PointInfo;
import com.example.eipaingestionapp.pooling.model.PointStatus;

public class TestUtils {
    public static PointInfo getPointInfo(String code, int statusCode, int availability, String timestamp) {
        PointInfo pointInfo = new PointInfo();
        pointInfo.setCode(code);
        PointStatus status = new PointStatus();
        status.setStatus(statusCode);
        status.setAvailability(availability);
        status.setTimestamp(timestamp);
        pointInfo.setStatus(status);
        return pointInfo;
    }
}
