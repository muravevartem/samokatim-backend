package com.muravev.samokatimmonolit.model.out;

import com.muravev.samokatimmonolit.model.OrganizationStatus;

import java.time.ZonedDateTime;

public record OrganizationFullOut(
        Long id,
        String name,
        String inn,
        String kpp,
        String email,
        String tel,
        OrganizationStatus status,
        ZonedDateTime createdAt

) {
}
