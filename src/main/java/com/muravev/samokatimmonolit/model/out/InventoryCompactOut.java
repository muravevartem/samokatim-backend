package com.muravev.samokatimmonolit.model.out;

import com.muravev.samokatimmonolit.model.InventoryStatus;

public record InventoryCompactOut(
        Long id,
        String alias,
        InventoryStatus status,
        InventoryModelFullOut model
) {
}
