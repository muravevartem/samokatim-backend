package com.muravev.samokatimmonolit.integration.yookassa.service;

import com.muravev.samokatimmonolit.entity.user.UserEntity;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Payment;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Refund;

import java.math.BigDecimal;

public interface YooKassaPaymentService {

    Payment hold(String orderId,
                 BigDecimal price,
                 String description,
                 String returnUrl,
                 UserEntity customer);

    Payment debit(String paymentId);

    void cancel(String paymentId);

    Payment getPaymentById(String id);

    Refund refund(String orderId,
                  String paymentId,
                  BigDecimal price,
                  String description,
                  UserEntity customer);

    Refund getRefundById(String id);
}
