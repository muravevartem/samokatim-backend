package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.muravev.samokatimmonolit.error.StatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorOut(
        StatusCode code,
        String message,
        String description
) {
}
