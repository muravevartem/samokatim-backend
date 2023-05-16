package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.entity.InventoryMonitoringEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.model.in.MapViewIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

public interface RentReader {
    Collection<RentEntity> findAllInViewAndRentedByMe(MapViewIn view);

    Page<RentEntity> findAllCompletedByClient(ClientEntity client, Pageable pageable);

    Page<RentEntity> findMyAll(Pageable pageable);

    List<RentEntity> findMyActiveAll();

    RentEntity findMyById(long id);

    SortedSet<InventoryMonitoringEntity> getTrack(long id);

    Page<RentEntity> findAll(long orgId, Pageable pageable);

    Page<RentEntity> findMyOrgAll(Pageable pageable);

    RentEntity findById(long id);
}
