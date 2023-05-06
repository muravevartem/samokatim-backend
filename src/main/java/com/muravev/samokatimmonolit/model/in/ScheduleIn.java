package com.muravev.samokatimmonolit.model.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.muravev.samokatimmonolit.util.JsonTimeFormat;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.OffsetTime;

public record ScheduleIn(
        @NotNull
        DayOfWeek day,
        @NotNull
        @JsonFormat(pattern = JsonTimeFormat.ISO_TIME)
        OffsetTime start,
        @NotNull
        @JsonFormat(pattern = JsonTimeFormat.ISO_TIME)
        OffsetTime end,

        boolean dayOff
) {
}
