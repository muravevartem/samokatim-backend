package com.muravev.samokatimmonolit.integration.dadata.service.impl;

import com.muravev.samokatimmonolit.integration.dadata.model.request.DadataAddressRequest;
import com.muravev.samokatimmonolit.integration.dadata.model.response.DadataAddressResponse;
import com.muravev.samokatimmonolit.integration.dadata.model.response.DadataSuggestionsAddressResponse;
import com.muravev.samokatimmonolit.integration.dadata.service.DadataAddressService;
import com.muravev.samokatimmonolit.model.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DadataAddressServiceImpl implements DadataAddressService {
    private static final String ADDRESS_ENDPOINT = "/suggestions/api/4_1/rs/geolocate/address";


    @Autowired
    @Qualifier("dadataClient")
    private RestTemplate dadataClient;


    @Override
    public List<Address> getFirstByGeoPoint(double lat, double lng) {
        ResponseEntity<DadataSuggestionsAddressResponse> response = dadataClient.postForEntity(
                ADDRESS_ENDPOINT,
                new DadataAddressRequest(lat, lng),
                DadataSuggestionsAddressResponse.class
        );

        DadataSuggestionsAddressResponse body = response.getBody();

        Objects.requireNonNull(body);

        List<DadataAddressResponse> suggestions = body.suggestions();
        if (suggestions.isEmpty())
            return List.of();

        return suggestions.stream()
                .map(suggestion -> new Address(suggestion.value(),
                        suggestion.fullValue(),
                        suggestion.data().postalCode(),
                        suggestion.data().timezone()))
                .toList();
    }
}
