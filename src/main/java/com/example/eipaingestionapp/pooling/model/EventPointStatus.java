package com.example.eipaingestionapp.pooling.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventPointStatus {
    private String pointId;
    private Status status;
    private String originalTs;
}
