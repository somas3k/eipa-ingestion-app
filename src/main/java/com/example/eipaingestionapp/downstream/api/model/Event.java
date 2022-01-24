package com.example.eipaingestionapp.downstream.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event<T> {
    private final String id = UUID.randomUUID().toString();
    private final String ts = Instant.now().toString();
    private T data;
}
