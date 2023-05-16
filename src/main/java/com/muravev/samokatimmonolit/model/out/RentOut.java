package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.muravev.samokatimmonolit.model.RentStatus;
import com.muravev.samokatimmonolit.util.JsonTimeFormat;

import java.time.ZonedDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RentOut(
        Long id,
        TariffOut tariff,
        @JsonFormat(pattern = JsonTimeFormat.ISO_DATE_TIME)
        ZonedDateTime startTime,
        @JsonFormat(pattern = JsonTimeFormat.ISO_DATE_TIME)
        ZonedDateTime endTime,
        InventoryFullOut inventory,
        List<GeoPositionOut> track,
        RentStatus status,
        PaymentOut cheque,
        DepositOut deposit
) {
}
