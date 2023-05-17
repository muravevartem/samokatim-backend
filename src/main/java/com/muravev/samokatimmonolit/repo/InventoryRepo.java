package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.user.ClientEntity;
import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepo extends JpaRepository<InventoryEntity, Long> {
    Page<InventoryEntity> findAllByOrganization(OrganizationEntity organization, Pageable pageable);

    @Query("""
            SELECT inventory FROM InventoryEntity inventory
            WHERE LOWER(inventory.alias) LIKE LOWER('%'||:keyword||'%')
                AND inventory.organization = :organization
            """)
    Page<InventoryEntity> findAllByOrganization(String keyword, OrganizationEntity organization, Pageable pageable);

    @Query("""
            SELECT inventory FROM InventoryEntity inventory
            WHERE inventory.lastMonitoringRecord.lng BETWEEN :lngSW AND :lngNE
                AND
                inventory.lastMonitoringRecord.lat BETWEEN :latSW AND :latNE
                AND
                inventory.status = :status
                AND
                inventory.organization.status = 'APPROVED'
            ORDER BY inventory.id DESC
            """)
    List<InventoryEntity> findAllByViewAndStatus(@Param("latNE") double latNE,
                                                 @Param("lngNE") double lngNE,
                                                 @Param("latSW") double latSW,
                                                 @Param("lngSW") double lngSW,
                                                 @Param("status") InventoryStatus status);

    @Query("""
            SELECT DISTINCT inventory FROM InventoryEntity inventory
            WHERE inventory.lastMonitoringRecord.lng BETWEEN :lngSW AND :lngNE
                AND
                inventory.lastMonitoringRecord.lat BETWEEN :latSW AND :latNE
                AND
                inventory.activeRent.client = :client
            ORDER BY inventory.id DESC
            """)
    List<InventoryEntity> findAllRentedByView(@Param("latNE") double latNE,
                                              @Param("lngNE") double lngNE,
                                              @Param("latSW") double latSW,
                                              @Param("lngSW") double lngSW,
                                              @Param("client") ClientEntity client);

    Optional<InventoryEntity> findByAlias(String alias);

}
