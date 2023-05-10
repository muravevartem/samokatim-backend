package com.muravev.samokatimmonolit.model.in.command.organization;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrganizationChangeLogoCommand(
        @NotNull UUID fileId
) {
}
