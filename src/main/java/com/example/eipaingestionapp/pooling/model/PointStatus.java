package com.example.eipaingestionapp.pooling.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PointStatus {
    private int availability;
    private int status;
    @JsonProperty("ts")
    private String timestamp;
}
