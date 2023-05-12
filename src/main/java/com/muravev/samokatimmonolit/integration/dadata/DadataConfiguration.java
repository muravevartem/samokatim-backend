package com.muravev.samokatimmonolit.integration.dadata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DadataConfiguration {

    @Value("${integration.dadata.base-url}")
    private String baseUrl;

    @Value("${integration.dadata.token}")
    private String token;

    @Bean
    RestTemplate dadataClient(RestTemplateBuilder builder) {
        return builder
                .rootUri(this.baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Token " + this.token)
                .build();
    }
}
