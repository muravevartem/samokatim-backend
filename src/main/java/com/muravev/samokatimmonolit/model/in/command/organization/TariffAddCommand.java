package com.muravev.samokatimmonolit.model.in.command.organization;

import com.muravev.samokatimmonolit.model.OrganizationTariffType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TariffAddCommand(
        String alias,
        @Positive
        BigDecimal price,
        @NotNull
        OrganizationTariffType type
) {
}
