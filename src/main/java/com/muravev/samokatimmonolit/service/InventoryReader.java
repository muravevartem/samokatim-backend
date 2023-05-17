package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.event.AbstractInventoryEvent;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.model.in.MapViewIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.SortedSet;

public interface InventoryReader {
    Collection<InventoryEntity> findAllInViewForRent(MapViewIn view);

    Collection<InventoryEntity> findAllInViewAndRentedByMe(MapViewIn view);

    Page<InventoryEntity> findAllAsEmployee(String keyword, Pageable pageable);

    InventoryEntity findByIdAsEmployee(long id);

    InventoryEntity findByIdAsClient(long id);

    InventoryEntity findById(long id);

    SortedSet<AbstractInventoryEvent> findEventsById(long id);

    Page<InventoryEntity> findAll(long org, String keyword, Pageable pageable);
}
