package com.muravev.samokatimmonolit.model.in.command.organization;

import jakarta.validation.constraints.NotNull;

public record TariffDeleteCommand(
        @NotNull long tariffId
) {
}
