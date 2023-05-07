package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.model.in.MapViewIn;
import com.muravev.samokatimmonolit.repo.RentRepo;
import com.muravev.samokatimmonolit.service.RentReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentReaderImpl implements RentReader {
    private final RentRepo rentRepo;
    private final SecurityService securityService;


    @Override
    @Transactional(readOnly = true)
    public Collection<RentEntity> findAllInViewAndRentedByMe(MapViewIn view) {
        ClientEntity currentClient = securityService.getCurrentClient();

        var northEast = view.getNorthEast();
        var southWest = view.getSouthWest();
        log.info("[GEO] getting location ne-lat: {} ne-lng: {} sw-lat: {} sw-lng: {}",
                northEast.lat(), northEast.lng(), southWest.lat(), southWest.lng());
        return rentRepo.findAllByView(
                northEast.lat(),
                northEast.lng(),
                southWest.lat(),
                southWest.lng(),
                currentClient
        );
    }
}
