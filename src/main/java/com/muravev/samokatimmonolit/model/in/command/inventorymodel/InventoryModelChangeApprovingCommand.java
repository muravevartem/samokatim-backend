package com.muravev.samokatimmonolit.model.in.command.inventorymodel;

import jakarta.validation.constraints.NotNull;

public record InventoryModelChangeApprovingCommand(
        @NotNull
        boolean approved
) {
}
