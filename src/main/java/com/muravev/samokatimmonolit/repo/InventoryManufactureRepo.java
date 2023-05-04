package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.InventoryManufactureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryManufactureRepo extends JpaRepository<InventoryManufactureEntity, Long> {
    Optional<InventoryManufactureEntity> findByNameIgnoreCase(String name);
}
