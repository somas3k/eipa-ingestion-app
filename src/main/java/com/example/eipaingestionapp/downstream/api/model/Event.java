package com.example.eipaingestionapp.downstream.api.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event<T> {
    private final String id = UUID.randomUUID().toString();
    private final String ts = Instant.now().toString();
    private final String type = "STATUS_UPDATE";
    private T data;
}
