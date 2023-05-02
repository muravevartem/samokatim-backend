package com.muravev.samokatimmonolit.integration.dadata.model.response;

public record DadataOrganizationDataResponse(
        DadataOrganizationDataNameResponse name,
        String kpp,
        String inn,
        String orgn

) {
}
