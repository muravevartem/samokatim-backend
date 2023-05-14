package com.muravev.samokatimmonolit.model.in.command.organization;

import com.muravev.samokatimmonolit.model.OrganizationTariffType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.List;

public record TariffChangeCommand(
        String alias,
        @Positive
        BigDecimal price,
        BigDecimal initialPrice,
        BigDecimal deposit,
        @NotNull
        OrganizationTariffType type,
        @NotEmpty
        List<DayOfWeek> days
) {
}
