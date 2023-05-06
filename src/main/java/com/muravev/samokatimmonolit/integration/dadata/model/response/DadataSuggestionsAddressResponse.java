package com.muravev.samokatimmonolit.integration.dadata.model.response;

import java.util.List;

public record DadataSuggestionsAddressResponse(
        List<DadataAddressResponse> suggestions
) {

}
