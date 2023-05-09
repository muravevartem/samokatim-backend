package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.OfficeEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.model.in.MapViewIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface OfficeReader {
    Collection<OfficeEntity> findAllInView(MapViewIn view);

    Collection<OfficeEntity> findMyAllInView(MapViewIn view);

    Page<OfficeEntity> getAllMyOffices(Pageable pageable);
    Page<OfficeEntity> getAllMyOffices(String keyword, Pageable pageable);
    OfficeEntity getOneMy(long id);
}
