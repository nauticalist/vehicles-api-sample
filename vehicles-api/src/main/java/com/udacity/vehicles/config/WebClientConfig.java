package com.udacity.vehicles.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOG = LoggerFactory.getLogger(WebClientConfig.class);
    private final String mapsServiceUrl;

    @Autowired
    private EurekaClient discoveryClient;

    @Autowired
    public WebClientConfig(
            @Value("${maps.endpoint}") String mapsEndpoint) {
        mapsServiceUrl = mapsEndpoint;
    }

    private String getPricingServiceUrl() {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka("PRICING-SERVICE", false);
        LOG.info(instance.getHomePageUrl());
        return instance.getHomePageUrl();
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
        return getWebClient(this.getPricingServiceUrl());
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
