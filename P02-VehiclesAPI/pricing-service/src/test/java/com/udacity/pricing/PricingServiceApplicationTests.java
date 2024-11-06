package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceApplicationTests {

	private static final Long VEHICLE_ID = 100L;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PriceRepository repo;

	@Test
	public void getPriceForId() {

		repo.save(new Price("USD", new BigDecimal(20000),VEHICLE_ID));
		Price price = repo.findById(VEHICLE_ID).get();

		ResponseEntity<Price> response = restTemplate.getForEntity("http://localhost:" + port + "/prices/" + VEHICLE_ID, Price.class);

		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertEquals(price.getPrice(), response.getBody().getPrice());

	}

}
