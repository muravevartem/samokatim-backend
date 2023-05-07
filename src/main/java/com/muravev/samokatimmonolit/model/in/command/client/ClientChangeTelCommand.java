package com.muravev.samokatimmonolit.model.in.command.client;

import jakarta.validation.constraints.NotBlank;

public record ClientChangeTelCommand(
        @NotBlank String value
) {
}
