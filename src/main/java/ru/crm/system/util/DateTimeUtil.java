package ru.crm.system.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class DateTimeUtil {

    /**
     * Метод для преобразования даты и времени из ISO в строку в формате "dd-MM-yyyy HH:mm"
     */
    public String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.getDayOfMonth() +
                "-" +
                dateTime.getMonthValue() +
                "-" +
                dateTime.getYear() +
                " " +
                dateTime.getHour() +
                ":" +
                dateTime.getMinute();
    }
}
