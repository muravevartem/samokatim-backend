package com.muravev.samokatimmonolit.integration.yookassa.service;

import com.muravev.samokatimmonolit.integration.yookassa.model.response.Payment;

import java.math.BigDecimal;

public interface YooKassaPaymentService {
    Payment createPayment(long rentId, BigDecimal price, String description);

    Payment getPaymentById(String id);
}
