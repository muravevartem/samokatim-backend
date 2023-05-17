package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface OrganizationReader {
    OrganizationEntity getByInn(String inn);

    OrganizationEntity getMyOrg();

    double getMyRevenue();

    Page<OrganizationEntity> getAll(String keyword, Pageable pageable);

    OrganizationEntity getOne(long id);
}
