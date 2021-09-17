package com.example.eipaingestionapp.pooling.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PriceInfo {
    private String price;
    private String unit;
    @JsonProperty("ts")
    private String timestamp;
    private String literal;
}
