package com.muravev.samokatimmonolit.integration.dadata.service;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;

import java.util.Optional;

public interface DadataOrganizationService {
    Optional<OrganizationEntity> getOneByInn(String inn);
}
