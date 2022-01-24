package com.example.eipaingestionapp.config;

import com.example.eipaingestionapp.downstream.DownstreamConfiguration;
import com.example.eipaingestionapp.pooling.PoolingConfiguration;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

@Configuration
@Import(value = {
        PoolingConfiguration.class,
        DownstreamConfiguration.class
})
public class AppConfiguration {
    @Bean("connector")
    public ClientHttpConnector getConnector() {
        return new ReactorClientHttpConnector(HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(10))
                        .addHandlerLast(new WriteTimeoutHandler(10))));
    }
}
