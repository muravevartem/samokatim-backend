package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.OfficeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OfficeRepo extends JpaRepository<OfficeEntity, Long> {
    Page<OfficeEntity> findAllByOrganization(OrganizationEntity organization, Pageable pageable);

    Optional<OfficeEntity> findAllByIdAndOrganization(Long id, OrganizationEntity organization);
}
