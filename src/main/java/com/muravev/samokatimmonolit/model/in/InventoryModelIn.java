package com.muravev.samokatimmonolit.model.in;

import jakarta.validation.constraints.NotNull;

public record InventoryModelIn(
        @NotNull
        Long id
) {
}
