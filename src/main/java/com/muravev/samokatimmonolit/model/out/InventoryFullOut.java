package com.muravev.samokatimmonolit.model.out;

import com.muravev.samokatimmonolit.model.InventoryStatus;

public record InventoryFullOut(
        Long id,
        String alias,
        InventoryModelFullOut model,
        OrganizationFullOut organization,
        InventoryStatus status
) {
}
