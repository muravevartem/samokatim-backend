package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record GeoPositionOut(
        double lat,
        double lng,
        @JsonProperty("timestamp")
        ZonedDateTime createdAt
) {
}
