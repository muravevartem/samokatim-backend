package com.muravev.samokatimmonolit.integration.yookassa.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ReceiptCustomerRequest(
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("email")
        String email

        ) {
}
