package com.udacity.pricing;

import com.udacity.pricing.domain.model.Price;
import com.udacity.pricing.domain.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PriceRepository priceRepository;


    @Test
    public void testInitializesRepositoryWithSampleData() {
        Iterable<Price> prices = priceRepository.findAll();
        assertThat(prices, is(iterableWithSize(3)));
        assertNotNull(prices.iterator().next().getId());
    }

    @Test
    public void testGetPriceByIdIsSuccessful() {
        ResponseEntity<Price> response = restTemplate.getForEntity("http://localhost:" + port + "/prices/1", Price.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().getPrice().intValue(), equalTo(10000));
    }
}
