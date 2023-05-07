package com.muravev.samokatimmonolit.model.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MapViewIn {
    @Valid
    @NotNull
    private GeoPointIn northEast;

    @Valid
    @NotNull
    private GeoPointIn southWest;
}
