package com.example.eipaingestionapp.downstream.api;

import com.example.eipaingestionapp.downstream.api.model.Event;

import java.util.List;

public interface Downstream<T> {
    void sendEvents(List<Event<T>> events);
}
