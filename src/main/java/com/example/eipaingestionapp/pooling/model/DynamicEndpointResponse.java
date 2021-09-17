package com.example.eipaingestionapp.pooling.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class DynamicEndpointResponse {
    private List<PointInfo> data;
    private String generated;
}
