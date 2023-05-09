package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.entity.InventoryMonitoringEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface InventoryMonitoringRepo extends JpaRepository<InventoryMonitoringEntity, Long> {
    @Query("""
            SELECT monitor FROM InventoryMonitoringEntity monitor
            WHERE monitor.createdAt BETWEEN :start AND :end
                AND monitor.inventory = :inventory
            """)
    List<InventoryMonitoringEntity> findAllByInventoryAndTime(InventoryEntity inventory, ZonedDateTime start, ZonedDateTime end);
}
