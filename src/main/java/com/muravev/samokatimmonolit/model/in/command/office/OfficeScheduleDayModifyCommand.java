package com.muravev.samokatimmonolit.model.in.command.office;

import com.muravev.samokatimmonolit.model.out.OfficeScheduleOut;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record OfficeScheduleDayModifyCommand(
        @Size(min = 7, max = 7)
        List<OfficeScheduleOut> days
) {
}
