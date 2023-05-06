package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.muravev.samokatimmonolit.util.JsonTimeFormat;

import java.time.DayOfWeek;
import java.time.OffsetTime;

public record OfficeScheduleOut(
        DayOfWeek day,
        @JsonFormat(pattern = JsonTimeFormat.ISO_TIME)
        OffsetTime start,
        @JsonFormat(pattern = JsonTimeFormat.ISO_TIME)
        OffsetTime end,
        boolean dayOff
) {
}
