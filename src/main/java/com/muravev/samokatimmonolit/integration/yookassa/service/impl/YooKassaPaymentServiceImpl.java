package com.muravev.samokatimmonolit.integration.yookassa.service.impl;

import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentConfirmationType;
import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentCurrency;
import com.muravev.samokatimmonolit.integration.yookassa.model.request.CreatePaymentConfirmationRequest;
import com.muravev.samokatimmonolit.integration.yookassa.model.request.CreatePaymentRequest;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Payment;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.PaymentAmount;
import com.muravev.samokatimmonolit.integration.yookassa.service.YooKassaPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@Slf4j
public class YooKassaPaymentServiceImpl implements YooKassaPaymentService {
    @Autowired
    @Qualifier("yookassaClient")
    private RestTemplate client;

    @Override
    public Payment createPayment(long rentId, BigDecimal price, String description) {
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .amount(new PaymentAmount(price, PaymentCurrency.RUB))
                .confirmation(
                        CreatePaymentConfirmationRequest.builder()
                                .type(PaymentConfirmationType.REDIRECT)
                                .returnUrl("http://localhost:3000")
                                .build()
                )
                .capture(false)
                .description(description)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Idempotence-Key", String.valueOf(Instant.now().toEpochMilli()));
        Payment payment = client.postForObject(
                "/v3/payments",
                new HttpEntity<>(request, httpHeaders),
                Payment.class
        );
        assert payment != null;

        log.info("Created payment {}", payment.id());
        return payment;
    }

    @Override
    public Payment getPaymentById(String id) {
        return client.getForObject(
                "/v3/payments/" + id,
                Payment.class
        );
    }
}
