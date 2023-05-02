package com.muravev.samokatimmonolit.integration.dadata.service.impl;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.integration.dadata.model.request.DadataOrganizationRequest;
import com.muravev.samokatimmonolit.integration.dadata.model.response.DadataOrganizationResponse;
import com.muravev.samokatimmonolit.integration.dadata.model.response.DadataSuggestionsResponse;
import com.muravev.samokatimmonolit.integration.dadata.service.DadataOrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Op;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DadataOrganizationServiceImpl implements DadataOrganizationService {
    private static final String INN_ENDPOINT = "/suggestions/api/4_1/rs/suggest/party";


    @Autowired
    @Qualifier("dadataClient")
    private RestTemplate dadataClient;


    @Override
    public Optional<OrganizationEntity> getOneByInn(String inn) {
        ResponseEntity<DadataSuggestionsResponse> response = dadataClient.postForEntity(
                INN_ENDPOINT,
                new DadataOrganizationRequest(inn),
                DadataSuggestionsResponse.class
        );
        DadataSuggestionsResponse body = response.getBody();

        Objects.requireNonNull(body);

        List<DadataOrganizationResponse> suggestions = body.suggestions();
        if (suggestions.isEmpty())
            return Optional.empty();

        return suggestions.stream()
                .map(suggestion -> new OrganizationEntity()
                        .setInn(suggestion.data().inn())
                        .setKpp(suggestion.data().kpp())
                        .setName(suggestion.data().name().shortName())
                        .setFullName(suggestion.data().name().fullName()))
                .findFirst();

    }
}
