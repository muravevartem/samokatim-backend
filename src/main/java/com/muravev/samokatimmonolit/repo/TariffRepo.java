package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.entity.OrganizationTariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TariffRepo extends JpaRepository<OrganizationTariffEntity, Long> {
    Optional<OrganizationTariffEntity> findByIdAndOrganization(Long id, OrganizationEntity organization);
}
