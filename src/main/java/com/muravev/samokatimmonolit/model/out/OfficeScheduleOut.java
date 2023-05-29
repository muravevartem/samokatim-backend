package com.muravev.samokatimmonolit.model.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.muravev.samokatimmonolit.util.JsonTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetTime;

public record   OfficeScheduleOut(
        DayOfWeek day,
        @JsonFormat(pattern = JsonTimeFormat.LOCAL_SHORT_TIME)
        LocalTime start,
        @JsonFormat(pattern = JsonTimeFormat.LOCAL_SHORT_TIME)
        LocalTime end,
        boolean dayOff
) {
}
