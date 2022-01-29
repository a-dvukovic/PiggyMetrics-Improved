package com.piggymetrics.statistics.client;

import com.netflix.loadbalancer.ServerList;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.commons.security.AccessTokenContextRelay;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;

import com.netflix.loadbalancer.Server;

@TestConfiguration
public class TestStatisticsConfig {
    @Bean
    public AccessTokenContextRelay accessTokenContextRelayBean() {
        return new AccessTokenContextRelay(null);
    }

    @Bean
    public ServerList<Server> ribbonServerList() {
        return new StaticServerList<>(new Server("localhost", 55502));
    }
}
