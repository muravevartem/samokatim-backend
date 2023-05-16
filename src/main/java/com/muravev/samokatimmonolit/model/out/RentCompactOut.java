package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.muravev.samokatimmonolit.util.JsonTimeFormat;

import java.time.ZonedDateTime;

public record RentCompactOut(
        Long id,
        @JsonFormat(pattern = JsonTimeFormat.ISO_DATE_TIME)
        ZonedDateTime startTime,
        @JsonFormat(pattern = JsonTimeFormat.ISO_DATE_TIME)
        ZonedDateTime endTime,
        InventoryCompactOut inventory,
        PaymentOut cheque
) {
}
