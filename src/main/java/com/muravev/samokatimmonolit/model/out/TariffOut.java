package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.muravev.samokatimmonolit.model.OrganizationTariffType;
import com.muravev.samokatimmonolit.util.JsonTimeFormat;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TariffOut(
        Long id,
        @JsonProperty("alias")
        String name,
        BigDecimal price,
        OrganizationTariffType type,
        @JsonFormat(pattern = JsonTimeFormat.ISO_DATE_TIME)
        ZonedDateTime createdAt
) {
}
