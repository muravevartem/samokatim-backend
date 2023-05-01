package com.muravev.samokatimmonolit.model.out;

import com.muravev.samokatimmonolit.model.InventoryType;

public record InventoryModelFullOut(
        Long id,
        String name,
        InventoryType type
) {
}
