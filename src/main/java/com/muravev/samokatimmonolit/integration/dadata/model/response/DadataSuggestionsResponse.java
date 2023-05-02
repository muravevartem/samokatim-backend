package com.muravev.samokatimmonolit.integration.dadata.model.response;

import java.util.List;

public record DadataSuggestionsResponse(
        List<DadataOrganizationResponse> suggestions
) {
}
