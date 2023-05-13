package com.muravev.samokatimmonolit.integration.yookassa.model.request;

import com.muravev.samokatimmonolit.integration.yookassa.model.response.PaymentAmount;
import lombok.Builder;

import java.util.Map;

@Builder
public record PaymentRequest(
        PaymentAmount amount,
        PaymentConfirmationRequest confirmation,
        boolean capture,
        String description,
        ReceiptRequest receipt,
        Map<String, String> metadata
) {
}
