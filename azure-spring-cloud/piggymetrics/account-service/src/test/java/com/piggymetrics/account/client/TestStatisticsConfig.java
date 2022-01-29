package com.piggymetrics.account.client;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.commons.security.AccessTokenContextRelay;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestStatisticsConfig {
    @Bean
    public AccessTokenContextRelay accessTokenContextRelayBean() {
        return new AccessTokenContextRelay(null);
    }
}
