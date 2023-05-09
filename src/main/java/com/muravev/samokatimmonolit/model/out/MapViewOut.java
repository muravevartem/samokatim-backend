package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MapViewOut(
        Collection<InventoryFullOut> free,
        Collection<RentOut> rented,
        Collection<OfficeFullOut> offices
) {
}
