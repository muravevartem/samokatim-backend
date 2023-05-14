package com.muravev.samokatimmonolit.integration.yookassa.service.impl;

import com.muravev.samokatimmonolit.entity.UserEntity;
import com.muravev.samokatimmonolit.integration.yookassa.error.InvalidStatusException;
import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentConfirmationType;
import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentCurrency;
import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentStatus;
import com.muravev.samokatimmonolit.integration.yookassa.model.request.*;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Payment;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.PaymentAmount;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Refund;
import com.muravev.samokatimmonolit.integration.yookassa.service.YooKassaPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class YooKassaPaymentServiceImpl implements YooKassaPaymentService {
    @Autowired
    @Qualifier("yookassaClient")
    private RestTemplate client;

    @Override
    public Payment hold(String orderId,
                        BigDecimal price,
                        String description,
                        String returnUrl,
                        UserEntity customer) {
        PaymentRequest request = PaymentRequest.builder()
                .amount(new PaymentAmount(price, PaymentCurrency.RUB))
                .confirmation(
                        PaymentConfirmationRequest.builder()
                                .type(PaymentConfirmationType.REDIRECT)
                                .returnUrl(returnUrl)
                                .build()
                )
                .capture(false)
                .description(description)
                .metadata(Map.of("order_id", orderId))
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        int hash = Objects.hash(orderId, Instant.now().getEpochSecond());
        httpHeaders.set("Idempotence-Key", String.valueOf(hash));
        Payment payment = client.postForObject(
                "/v3/payments",
                new HttpEntity<>(request, httpHeaders),
                Payment.class
        );
        assert payment != null;

        log.info("Hold payment {}", payment.id());
        return payment;
    }

    @Override
    @Retryable(retryFor = InvalidStatusException.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay =10000))
    public Payment debit(String paymentId) {
        Payment existedPayment = getPaymentById(paymentId);
        if (existedPayment.status() != PaymentStatus.WAITING_FOR_CAPTURE)
            throw new InvalidStatusException();

        PaymentRequest paymentRequest = PaymentRequest.builder().build();

        HttpHeaders httpHeaders = new HttpHeaders();
        int hash = Objects.hash(paymentId, Instant.now().toEpochMilli());
        httpHeaders.set("Idempotence-Key", String.valueOf(hash));
        Payment payment = client.postForObject(
                "/v3/payments/%s/capture".formatted(paymentId),
                new HttpEntity<>(paymentRequest, httpHeaders),
                Payment.class
        );
        log.info("Debit payment {}", paymentId);
        return payment;
    }

    @Override
    @Retryable(retryFor = InvalidStatusException.class, maxAttempts = Integer.MAX_VALUE, backoff = @Backoff(delay = 10000))
    public void cancel(String paymentId) {
        Payment existedPayment = getPaymentById(paymentId);
        if (existedPayment.status() != PaymentStatus.WAITING_FOR_CAPTURE)
            throw new InvalidStatusException();

        HttpHeaders httpHeaders = new HttpHeaders();
        int hash = Objects.hash(paymentId, Instant.now().toEpochMilli());
        httpHeaders.set("Idempotence-Key", String.valueOf(hash));
        client.postForObject(
                "/v3/payments/%s/cancel".formatted(paymentId),
                new HttpEntity<>(Map.of(), httpHeaders),
                Void.class
        );
    }

    @Override
    public Payment getPaymentById(String id) {
        return client.getForObject(
                "/v3/payments/" + id,
                Payment.class
        );
    }

    @Override
    public Refund refund(String orderId,
                         String paymentId,
                         BigDecimal price,
                         String description,
                         UserEntity customer) {
        RefundRequest refundRequest = RefundRequest.builder()
                .paymentId(paymentId)
                .amount(new PaymentAmount(price, PaymentCurrency.RUB))
                .description(description)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        int hash = Objects.hash(paymentId, Instant.now().toEpochMilli());
        httpHeaders.set("Idempotence-Key", String.valueOf(hash));
        Refund refund = client.postForObject(
                "/v3/refunds",
                new HttpEntity<>(refundRequest, httpHeaders),
                Refund.class
        );
        log.info("Refund {}", refund.id());
        return refund;
    }

    @Override
    public Refund getRefundById(String id) {
        return client.getForObject(
                "/v3/refunds/" + id,
                Refund.class
        );
    }
}
