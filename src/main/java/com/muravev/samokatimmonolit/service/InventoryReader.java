package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.event.AbstractInventoryEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.SortedSet;

public interface InventoryReader {
    Page<InventoryEntity> findAllAsEmployee(String keyword, Pageable pageable);

    InventoryEntity findByIdAsEmployee(long id);

    InventoryEntity findByIdAsClient(long id);

    SortedSet<AbstractInventoryEvent> findEventsById(long id);
}
