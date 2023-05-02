package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepo extends JpaRepository<OrganizationEntity, Long> {
    Optional<OrganizationEntity> findByInn(String inn);
}
