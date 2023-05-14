package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.OrganizationEntity;

public interface OrganizationReader {
    OrganizationEntity getByInn(String inn);

    OrganizationEntity getMyOrg();

    double getMyRevenue();
}
