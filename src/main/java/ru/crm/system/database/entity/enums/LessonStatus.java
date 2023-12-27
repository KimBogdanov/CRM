package ru.crm.system.database.entity.enums;

/**
 * статус проведения урока - удачно, неудачно, по какой причине
 */
public enum LessonStatus {
    APPOINTED,
    SUCCESSFULLY_FINISHED,
    CANCELED,
    CANCELED_FOR_GOOD_REASON,
    CANCELED_BY_TEACHER_FAULT
}
