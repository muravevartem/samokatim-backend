package com.muravev.samokatimmonolit.model.in.command.inventory;

import jakarta.validation.constraints.NotBlank;

public record InventoryChangeAliasCommand(
        @NotBlank
        String alias
) {
}
