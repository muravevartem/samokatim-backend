package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<OrganizationEntity, Long> {
}
