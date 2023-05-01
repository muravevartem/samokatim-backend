package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryReader {
    Page<InventoryEntity> findAllAsEmployee(String keyword, Pageable pageable);

    InventoryEntity findByIdAsEmployee(long id);

    InventoryEntity findByIdAsClient(long id);

}
