package com.muravev.samokatimmonolit.model.out;

import java.util.List;
import java.util.Set;

public record OfficeFullOut(
        Long id,
        String alias,
        double lat,
        double lng,
        String address,
        int capacity,
        boolean closed,
        OrganizationCompactOut organization,
        Set<InventoryFullOut> inventories,
        List<OfficeScheduleOut> schedules
) {
}
