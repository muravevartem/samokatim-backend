package com.muravev.samokatimmonolit.integration.dadata.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DadataAddressDataResponse(
        @JsonProperty("postal_code")
        String postalCode,
        @JsonProperty("timezone")
        String timezone
) {
}
