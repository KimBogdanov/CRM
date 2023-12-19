package ru.crm.system.database.entity.enums;

public enum OrderStatus {
    UNPROCESSED,
    SCHEDULE_APPOINTMENT,
    APPOINTMENT_SCHEDULED,
    APPOINTMENT_COMPLETED,
    SUCCESSFULLY_COMPLETED,
    POOR_LEAD,
    REFUSED_TO_PURCHASE,
    RESERVED
}