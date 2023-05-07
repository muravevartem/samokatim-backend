package com.muravev.samokatimmonolit.model.in.command.inventory;

import jakarta.validation.constraints.NotNull;

public record InventoryAddTariffCommand(
        @NotNull Long tariffId
) {
}
