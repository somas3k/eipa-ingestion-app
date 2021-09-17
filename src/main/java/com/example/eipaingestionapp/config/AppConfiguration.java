package com.example.eipaingestionapp.config;

import com.example.eipaingestionapp.downstream.DownstreamConfiguration;
import com.example.eipaingestionapp.pooling.PoolingConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        PoolingConfiguration.class,
        DownstreamConfiguration.class
})
public class AppConfiguration {
}
