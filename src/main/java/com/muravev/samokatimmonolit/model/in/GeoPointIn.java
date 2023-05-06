package com.muravev.samokatimmonolit.model.in;

import jakarta.validation.constraints.NotNull;

public record GeoPointIn(
        @NotNull double lat,
        @NotNull double lng
) {
}
