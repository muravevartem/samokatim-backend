package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.OfficeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OfficeReader {

    Page<OfficeEntity> getAllMyOffices(Pageable pageable);

    OfficeEntity getOneMy(long id);
}
