package com.muravev.samokatimmonolit.model.out;

import java.util.Collection;
import java.util.List;

public record MapViewOut(
        Collection<InventoryFullOut> free,
        Collection<RentOut> rented
) {
}
