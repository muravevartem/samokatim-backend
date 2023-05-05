package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.util.JsonTimeFormat;

import java.time.ZonedDateTime;
import java.util.List;

public record InventoryFullOut(
        Long id,
        String alias,
        InventoryModelFullOut model,
        OrganizationFullOut organization,
        InventoryStatus status,
        boolean supportsTelemetry,
        GeoPositionOut lastMonitoringRecord,
        List<InventoryEventOut> events,
        @JsonFormat(pattern = JsonTimeFormat.ISO_DATE_TIME)
        ZonedDateTime createdAt
) {
}
