package com.muravev.samokatimmonolit.model.out;

import com.muravev.samokatimmonolit.model.PaymentStatus;

import java.math.BigDecimal;

public record PaymentOut(
        Long id,
        BigDecimal price,
        PaymentStatus status,
        String bankId
) {
}
