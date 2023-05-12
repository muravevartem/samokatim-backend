package com.muravev.samokatimmonolit.integration.yookassa.model.request;

import com.muravev.samokatimmonolit.integration.yookassa.model.response.PaymentAmount;
import lombok.Builder;

@Builder
public record CreatePaymentRequest(
        PaymentAmount amount,
        CreatePaymentConfirmationRequest confirmation,
        boolean capture,
        String description
) {
}
