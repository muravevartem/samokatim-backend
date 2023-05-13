package com.muravev.samokatimmonolit.integration.yookassa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RefundStatus {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("succeeded")
    SUCCEEDED,
    @JsonProperty("canceled")
    CANCELED
}
