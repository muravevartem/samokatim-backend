package com.muravev.samokatimmonolit.model.in.command.office;

import com.muravev.samokatimmonolit.model.in.GeoPointIn;
import com.muravev.samokatimmonolit.model.in.ScheduleIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record OfficeCreateCommand(
        @Valid @NotNull GeoPointIn location,
        @NotBlank String alias,
        @NotEmpty Set<@Valid ScheduleIn> schedules,
        @Positive int capacity

) {
}
