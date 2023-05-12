package com.muravev.samokatimmonolit.integration.yookassa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentStatus {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("waiting_for_capture")
    WAITING_FOR_CAPTURE,
    @JsonProperty("succeeded")
    SUCCEEDED,
    @JsonProperty("canceled")
    CANCELED
}
