package com.muravev.samokatimmonolit.integration.dadata.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DadataAddressResponse(
        String value,
        @JsonProperty("unrestricted_value")
        String fullValue,
        DadataAddressDataResponse data
) {
}
