package com.muravev.samokatimmonolit.report.model.out;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentStatOut(
        LocalDate date,
        Long amount,
        BigDecimal moneySum,
        BigDecimal moneyAvg
) {
}
