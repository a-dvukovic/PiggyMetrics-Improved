package com.piggymetrics.account.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.netflix.loadbalancer.ServerList;
import com.piggymetrics.account.domain.Account;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.netflix.loadbalancer.Server;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

/**
 * @author cdov
 */

@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
@SpringBootTest(classes = {
        StatisticsServiceClient.class, StatisticsServiceClientFallback.class,
        TestStatisticsConfig.class},
        properties = {"feign.hystrix.enabled=true", "eureka.client.enabled=false", "feign.circuitbreaker.enabled=true"
})
public class StatisticsServiceClientFallbackTest {
    @Autowired
    private StatisticsServiceClient statisticsServiceClient;

    static private WireMockServer wireMockServer;

    /*
    @Autowired
    public void setWireMockServer(WireMockServer wireMockServer) {
        this.wireMockServer = wireMockServer;
    }
    */

    @BeforeAll
    static void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        wireMockServer.stubFor(put(urlEqualTo("/statistics/"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")));
    }

    @AfterAll
    static  void tearDown() {
        wireMockServer.stop();
        wireMockServer = null;
    }

    @DirtiesContext
    @Test
    public void testUpdateStatisticsWithFailFallback(CapturedOutput outputCapture){
        statisticsServiceClient.updateStatistics("test", new Account());

        //assertThat(outputCapture.getOut()).contains("Error during update statistics for account: test");
        assertTrue(outputCapture.getOut().contains("Error during update statistics for account: test"));

    }


    @TestConfiguration
    public static class LocalRibbonClientConfiguration {
        @Bean
        public ServerList<Server> ribbonServerList() {
            return new StaticServerList<>(new Server("localhost", wireMockServer.port()));
        }
    }

}

