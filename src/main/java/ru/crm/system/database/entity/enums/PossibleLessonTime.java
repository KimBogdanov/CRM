package ru.crm.system.database.entity.enums;

import java.time.LocalTime;

public enum PossibleLessonTime {
    NINE_00(LocalTime.of(9, 0)),
    TEN_00(LocalTime.of(10, 0)),
    ELEVEN_00(LocalTime.of(11, 0)),
    TWELVE_00(LocalTime.of(12, 0)),
    THIRTEEN_00(LocalTime.of(13, 0)),
    FOURTEEN_00(LocalTime.of(14, 0)),
    FIFTEEN_00(LocalTime.of(15, 0)),
    SIXTEEN_00(LocalTime.of(16, 0)),
    SEVENTEEN_00(LocalTime.of(17, 0));

    final LocalTime lessonTime;

    PossibleLessonTime(LocalTime lessonTime) {
        this.lessonTime = lessonTime;
    }
}