package ru.crm.system.database.entity.enums;

/**
 * статус заказа - удачен, неудачен и по какой причине
 */
public enum OrderStatus {
    UNPROCESSED,
    APPOINTMENT_SCHEDULED,
    APPOINTMENT_COMPLETED,
    SUCCESSFULLY_COMPLETED,
    POOR_LEAD,
    REFUSED_TO_PURCHASE,
    RESERVED
}