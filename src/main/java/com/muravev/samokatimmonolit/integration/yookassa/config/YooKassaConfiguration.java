package com.muravev.samokatimmonolit.integration.yookassa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class YooKassaConfiguration {
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${integration.yookassa.username}")
    private String username;

    @Value("${integration.yookassa.secret-key}")
    private String secretKey;

    @Bean
    @Qualifier("yookassaClient")
    RestTemplate yookassaClient() {
        return restTemplateBuilder
                .rootUri("https://api.yookassa.ru")
                .basicAuthentication(username, secretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE)
                .build();
    }
}
