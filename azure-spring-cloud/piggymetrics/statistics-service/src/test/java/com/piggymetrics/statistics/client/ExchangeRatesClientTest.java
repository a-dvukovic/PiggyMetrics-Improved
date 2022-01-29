package com.piggymetrics.statistics.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.common.collect.ImmutableMap;
import com.netflix.loadbalancer.ServerList;
import com.piggymetrics.statistics.domain.Currency;
import com.piggymetrics.statistics.domain.ExchangeRatesContainer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.netflix.loadbalancer.Server;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ImportAutoConfiguration({RibbonAutoConfiguration.class, FeignAutoConfiguration.class})
@EnableFeignClients
@WireMockTest(httpPort = 55502)
@ContextConfiguration (classes = {TestStatisticsConfig.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {ExchangeRatesClient.class, ExchangeRatesClientFallback.class, FeignConfig.class},
		properties = {"feign.hystrix.enabled=true", "eureka.client.enabled=false", "feign.circuitbreaker.enabled=true"})
public class ExchangeRatesClientTest {

	@Autowired
	private ExchangeRatesClient client;

	static private ExchangeRatesContainer container = new ExchangeRatesContainer();

	static private Map<String, BigDecimal> rates = ImmutableMap.of(
	Currency.EUR.name(), BigDecimal.ONE,
	Currency.RUB.name(), BigDecimal.TEN,
	Currency.USD.name(), BigDecimal.ONE);

	//static private WireMockServer wireMockServer;

	private String convertResponseToString(HttpResponse response) throws IOException {
		InputStream responseStream = response.getEntity().getContent();
		Scanner scanner = new Scanner(responseStream, "UTF-8");
		String responseString = scanner.useDelimiter("\\Z").next();
		scanner.close();
		return responseString;
	}

	@BeforeAll
	static void setUp() {
		container.setBase(Currency.getBase());
		container.setRates(rates);

		/*
		wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(55501));
		wireMockServer.start();
		WireMock.configureFor("localhost", wireMockServer.port());


		wireMockServer.stubFor(get(urlEqualTo("/latest?base=1"))
				.willReturn(aResponse()
						.withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", "application/json")
						.withStatus(200)
						.withBody(container.toString().getBytes())));

		 */
	}

	@AfterAll
	static  void tearDown() {
		//wireMockServer.stop();
		//wireMockServer = null;
	}

	@Test
	public void shouldRetrieveExchangeRates(WireMockRuntimeInfo wmRuntimeInfo) {


		stubFor(get(urlPathEqualTo ("/latest"))
				.withQueryParam("base", equalTo("USD"))
				.willReturn(aResponse()
						.withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", "application/json;charset=UTF-8")
						.withStatus(200)
						.withBody(container.toString().getBytes())));

		System.out.println(wmRuntimeInfo.getHttpBaseUrl());
		System.out.println(wmRuntimeInfo.getWireMock().allStubMappings().toString());

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet request = new HttpGet("http://localhost:55502/latest?base=USD");
		try {
			HttpResponse httpResponse = httpClient.execute(request);
			String responseString = convertResponseToString(httpResponse);
			System.out.println("===responseString");
			System.out.println(responseString);
			System.out.println("===responseString");

			verify(getRequestedFor(urlPathEqualTo("/latest")).withQueryParam("base", equalTo("USD"))
					);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExchangeRatesContainer container = client.getRates(Currency.getBase());

		System.out.println(container);
		assertEquals(container.getDate(), LocalDate.now());
		assertEquals(container.getBase(), Currency.getBase());

		assertNotNull(container.getRates());
		assertNotNull(container.getRates().get(Currency.USD.name()));
		assertNotNull(container.getRates().get(Currency.EUR.name()));
		assertNotNull(container.getRates().get(Currency.RUB.name()));
	}

	@Test
	public void shouldRetrieveExchangeRatesForSpecifiedCurrency() {

		stubFor(get(urlPathEqualTo ("/latest"))
				.withQueryParam("base", equalTo("USD"))
				.willReturn(aResponse()
						.withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", "application/json;charset=UTF-8")
						.withStatus(200)
						.withBody(container.toString().getBytes())));

		Currency requestedCurrency = Currency.EUR;
		ExchangeRatesContainer container = client.getRates(Currency.getBase());

		assertEquals(container.getDate(), LocalDate.now());
		assertEquals(container.getBase(), Currency.getBase());

		assertNotNull(container.getRates());
		assertNotNull(container.getRates().get(requestedCurrency.name()));
	}

}