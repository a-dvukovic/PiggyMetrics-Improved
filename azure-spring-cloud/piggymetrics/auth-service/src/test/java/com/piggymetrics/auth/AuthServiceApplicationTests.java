package com.piggymetrics.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding: true", "spring.data.mongodb.database: piggymetrics", "spring.data.mongodb.port: 0", "eureka.client.enabled=false","spring.cloud.config.enabled=false"})
public class AuthServiceApplicationTests {


	@Test
	public void contextLoads() {
	}

}

