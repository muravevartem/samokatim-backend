package com.muravev.samokatimmonolit.model.in.command.rent;

import jakarta.validation.constraints.NotNull;

public record RentCreateCommand(
        @NotNull Long inventoryId,
        @NotNull Long tariffId
) {
}
