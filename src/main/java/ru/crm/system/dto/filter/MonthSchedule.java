package ru.crm.system.dto.filter;

import lombok.Getter;

import java.time.Month;
import java.time.Year;

@Getter
public enum MonthSchedule {
    JANUARY(Month.JANUARY),
    FEBRUARY(Month.FEBRUARY),
    MARCH(Month.MARCH),
    APRIL(Month.APRIL),
    MAY(Month.MAY),
    JULY(Month.JULY),
    JUNE(Month.JUNE),
    AUGUST(Month.AUGUST),
    SEPTEMBER(Month.SEPTEMBER),
    OCTOBER(Month.OCTOBER),
    NOVEMBER(Month.NOVEMBER),
    DECEMBER(Month.DECEMBER);

    public static final Year year = Year.now();

    private final Month month;

    MonthSchedule(Month month) {
        this.month = month;
    }
}