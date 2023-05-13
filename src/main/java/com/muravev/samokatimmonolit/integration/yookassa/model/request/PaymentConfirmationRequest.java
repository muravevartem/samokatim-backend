package com.muravev.samokatimmonolit.integration.yookassa.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentConfirmationType;
import lombok.Builder;

@Builder
public record PaymentConfirmationRequest(
        PaymentConfirmationType type,
        @JsonProperty("return_url")
        String returnUrl
) {
}
