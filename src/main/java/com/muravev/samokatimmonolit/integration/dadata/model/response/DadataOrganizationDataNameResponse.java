package com.muravev.samokatimmonolit.integration.dadata.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public record DadataOrganizationDataNameResponse(
        @JsonProperty("full_with_opf")
        String fullName,
        @JsonProperty("short_with_opf")
        String shortName
) {
}
