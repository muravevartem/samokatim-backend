package com.muravev.samokatimmonolit.model.in;

import jakarta.validation.constraints.NotNull;

public record OrganizationIn(
        @NotNull
        Long id
) {
}
