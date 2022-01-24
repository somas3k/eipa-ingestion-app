package com.example.eipaingestionapp.downstream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DownstreamConfiguration {
    private final ClientHttpConnector connector;
    @Value("${downstream.url.endpoint}")
    private String downstreamEndpointUrl;

    public DownstreamConfiguration(ClientHttpConnector connector) {
        this.connector = connector;
    }

    @Bean("downstreamClient")
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(downstreamEndpointUrl)
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
