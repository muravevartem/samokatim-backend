package com.muravev.samokatimmonolit.integration.dadata.model.response;

import java.util.List;

public record DadataSuggestionsOrganizationResponse(
        List<DadataOrganizationResponse> suggestions
) {
}
