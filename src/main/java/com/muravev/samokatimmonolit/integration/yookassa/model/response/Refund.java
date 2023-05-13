package com.muravev.samokatimmonolit.integration.yookassa.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muravev.samokatimmonolit.integration.yookassa.model.RefundStatus;

public record Refund(
        String id,
        @JsonProperty("payment_id")
        String paymentId,
        PaymentAmount amount,
        String description,
        RefundStatus status

) {
}
