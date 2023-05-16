package com.muravev.samokatimmonolit.model.out;

import com.muravev.samokatimmonolit.model.DepositStatus;

import java.math.BigDecimal;

public record DepositOut(
        Long id,
        BigDecimal price,
        String bankId,
        String refundBankId,
        DepositStatus status
) {
}
