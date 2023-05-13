package com.muravev.samokatimmonolit.integration.yookassa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.PaymentAmount;
import lombok.Builder;

@Builder
public record PaymentItem(
        String description,
        PaymentAmount amount,
        @JsonProperty("vat_code")
        int vatCode,
        String quantity
) {
}
