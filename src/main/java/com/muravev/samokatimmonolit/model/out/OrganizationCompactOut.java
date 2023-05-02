package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrganizationCompactOut(
        Long id,
        String name,
        String fullName,
        String inn,
        String kpp
) {
}
