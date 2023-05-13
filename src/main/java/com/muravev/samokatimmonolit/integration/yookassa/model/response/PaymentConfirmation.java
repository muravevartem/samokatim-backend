package com.muravev.samokatimmonolit.integration.yookassa.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentConfirmationType;

public record PaymentConfirmation(
        PaymentConfirmationType type,
        @JsonProperty("confirmation_token")
        String token,
        @JsonProperty("confirmation_url")
        String url
) {
}
