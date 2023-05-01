package com.muravev.samokatimmonolit.model.in.command.inventorymodel;

import com.muravev.samokatimmonolit.model.InventoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryModelCreateCommand(
        @NotBlank
        String name,
        @NotNull
        InventoryType type,
        @NotBlank
        String manufacture
) {
}
