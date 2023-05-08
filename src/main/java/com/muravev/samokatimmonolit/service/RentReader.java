package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.model.in.MapViewIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface RentReader {
    Collection<RentEntity> findAllInViewAndRentedByMe(MapViewIn view);

    Page<RentEntity> findAllCompletedByClient(ClientEntity client, Pageable pageable);

    Page<RentEntity> findMyAll(Pageable pageable);

    RentEntity findMyById(long id);
}
