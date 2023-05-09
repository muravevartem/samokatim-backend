package com.muravev.samokatimmonolit.model.out;

import java.math.BigDecimal;

public record ChequeOut(
        Long id,
        BigDecimal price
) {
}
