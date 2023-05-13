package com.muravev.samokatimmonolit.integration.yookassa.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.PaymentAmount;
import lombok.Builder;

@Builder
public record RefundRequest(
        @JsonProperty("payment_id")
        String paymentId,
        PaymentAmount amount,
        ReceiptRequest receipt,
        String description
) {
}
