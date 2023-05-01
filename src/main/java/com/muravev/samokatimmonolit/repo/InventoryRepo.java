package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InventoryRepo extends JpaRepository<InventoryEntity, Long> {
    Page<InventoryEntity> findAllByOrganization(OrganizationEntity organization, Pageable pageable);

    @Query("""
            SELECT inventory FROM InventoryEntity inventory
            WHERE LOWER(inventory.alias) LIKE LOWER('%'||:keyword||'%')
                AND inventory.organization = :organization
            """)
    Page<InventoryEntity> findAllByOrganization(String keyword, OrganizationEntity organization, Pageable pageable);

}
