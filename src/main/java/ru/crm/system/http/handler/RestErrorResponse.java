package ru.crm.system.http.handler;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record RestErrorResponse(String message,
                                HttpStatus status,
                                LocalDateTime thrownAt) {
}