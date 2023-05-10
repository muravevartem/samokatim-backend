package com.muravev.samokatimmonolit.model.out;

import com.muravev.samokatimmonolit.model.OrganizationStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public record OrganizationFullOut(
        Long id,
        String name,
        String inn,
        String kpp,
        String email,
        String tel,
        FileOut logo,
        OrganizationStatus status,
        Set<TariffOut> tariffs,
        ZonedDateTime createdAt

) {
}
