package com.muravev.samokatimmonolit.integration.yookassa.model.response;

import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentCurrency;

import java.math.BigDecimal;

public record PaymentAmount(
        BigDecimal value,
        PaymentCurrency currency
) {
}
