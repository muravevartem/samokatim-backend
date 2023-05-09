package com.muravev.samokatimmonolit.model.out;

public record InventoryCompactOut(
        Long id,
        InventoryModelFullOut model
) {
}
