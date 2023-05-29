package com.muravev.samokatimmonolit.model.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.muravev.samokatimmonolit.util.JsonTimeFormat;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetTime;

public record ScheduleIn(
        @NotNull
        DayOfWeek day,
        @NotNull
        @JsonFormat(pattern = JsonTimeFormat.LOCAL_TIME)
        LocalTime start,
        @NotNull
        @JsonFormat(pattern = JsonTimeFormat.LOCAL_TIME)
        LocalTime end,

        boolean dayOff
) {
}
