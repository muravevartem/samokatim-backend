package com.muravev.samokatimmonolit.integration.yookassa.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentStatus;
import com.muravev.samokatimmonolit.util.JsonTimeFormat;

import java.time.ZonedDateTime;

public record Payment(
        String id,
        PaymentStatus status,
        boolean paid,
        PaymentAmount amount,
        PaymentConfirmation confirmation,
        String description,
        @JsonProperty("created_at")
        @JsonFormat(pattern = JsonTimeFormat.ISO_DATE_TIME)
        ZonedDateTime createdAt
) {
}
