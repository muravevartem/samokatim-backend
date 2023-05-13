package com.muravev.samokatimmonolit.model.out;

public record PaymentOptionsOut(
        long rentId,
        String confirmationUrl
) {
}
