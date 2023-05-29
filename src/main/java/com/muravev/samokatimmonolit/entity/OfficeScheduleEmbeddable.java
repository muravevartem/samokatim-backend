package com.muravev.samokatimmonolit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetTime;

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class OfficeScheduleEmbeddable {
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private DayOfWeek day;

    @Column(name = "start_time")
    private LocalTime start;

    @Column(name = "end_time")
    private LocalTime end;

    @Column(name = "is_day_off", nullable = false)
    private boolean dayOff;


    public OfficeScheduleEmbeddable setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
        if (this.dayOff) {
            start = null;
            end = null;
        }
        return this;
    }

    public OfficeScheduleEmbeddable setStart(LocalTime start) {
        this.start = dayOff ? null : start;
        return this;
    }

    public OfficeScheduleEmbeddable setEnd(LocalTime end) {
        this.end = dayOff ? null : end;
        return this;
    }
}
