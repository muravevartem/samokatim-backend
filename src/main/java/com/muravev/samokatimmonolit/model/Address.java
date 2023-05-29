package com.muravev.samokatimmonolit.model;

public record Address(
        String value,
        String fullValue,
        String postalCode,
        String timezone
) {
}
