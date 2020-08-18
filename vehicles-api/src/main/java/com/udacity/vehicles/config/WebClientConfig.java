package com.udacity.vehicles.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
    private final String mapsServiceUrl;
    private final String pricingServiceUrl;

    @Autowired
    public WebClientConfig(
            @Value("${maps.endpoint}") String mapsEndpoint,
            @Value("${pricing.endpoint}") String pricingEndpoint) {
        mapsServiceUrl = mapsEndpoint;
        pricingServiceUrl = pricingEndpoint;
    }

    /**
     * Web Client for the maps (location) API
     * @return created maps endpoint
     */
    @Bean(name = "maps")
    WebClient webClientMaps() {
        return getWebClient(mapsServiceUrl);
    }

    /**
     * Web Client for the pricing API
     * @return created pricing endpoint
     */
    @Bean(name = "pricing")
    WebClient webClientPricing() {
        return getWebClient(pricingServiceUrl);
    }

    private WebClient getWebClient(String serviceUrl) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                                .doOnConnected(conn -> conn
                                        .addHandlerLast(new ReadTimeoutHandler(10))
                                        .addHandlerLast(new WriteTimeoutHandler(10))));

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(connector)
                .baseUrl(serviceUrl)
                //.filter(lbFunction)
                .build();
    }
}
