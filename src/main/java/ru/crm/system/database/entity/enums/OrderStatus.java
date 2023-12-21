package ru.crm.system.database.entity.enums;

/**
 * статус заказа - удачен, неудачен и по какой причине
 */
public enum OrderStatus {
    NOT_PROCESSED,
    SET_APPOINTMENT,
    SUCCESSFUL_MEETING,
    SUCCESSFULLY_COMPLETED,
    REFUSED,
    DEAD
}