package com.example.eipaingestionapp.pooling;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PoolingConfiguration {
    private final ClientHttpConnector connector;
    @Value("${eipa.export.dynamic.url}")
    private String eipaUrl;
    @Value("${api.key}")
    private String apiKey;

    public PoolingConfiguration(ClientHttpConnector connector) {
        this.connector = connector;
    }

    @Bean("poolingClient")
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(eipaUrl.concat("/").concat(apiKey))
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }
}
