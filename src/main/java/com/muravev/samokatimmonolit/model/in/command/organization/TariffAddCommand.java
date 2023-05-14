package com.muravev.samokatimmonolit.model.in.command.organization;

import com.muravev.samokatimmonolit.model.OrganizationTariffType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.List;

@Builder
public record TariffAddCommand(
        String alias,
        @Positive
        BigDecimal price,
        BigDecimal initialPrice,
        BigDecimal deposit,
        @NotNull
        OrganizationTariffType type,

        List<DayOfWeek> days
) {
}
