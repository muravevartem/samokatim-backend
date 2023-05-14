package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.muravev.samokatimmonolit.model.OrganizationTariffType;
import com.muravev.samokatimmonolit.util.JsonTimeFormat;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record TariffOut(
        Long id,
        @JsonProperty("alias")
        String name,
        BigDecimal price,
        BigDecimal initialPrice,
        BigDecimal deposit,
        OrganizationTariffType type,
        List<DayOfWeek> days,
        @JsonFormat(pattern = JsonTimeFormat.ISO_DATE_TIME)
        ZonedDateTime createdAt
) {
}
