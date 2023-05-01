package com.muravev.samokatimmonolit.model.in.command.inventory;

import com.muravev.samokatimmonolit.model.InventoryClass;
import com.muravev.samokatimmonolit.model.in.InventoryModelIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record InventoryChangeClassCommand(
        @NotNull
        InventoryClass inventoryClass
) {
}
