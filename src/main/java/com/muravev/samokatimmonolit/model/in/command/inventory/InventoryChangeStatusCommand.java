package com.muravev.samokatimmonolit.model.in.command;

import com.muravev.samokatimmonolit.model.InventoryStatus;
import jakarta.validation.constraints.NotNull;

public record InventoryChangeStatusCommand(
        @NotNull
        InventoryStatus status
) {
}
