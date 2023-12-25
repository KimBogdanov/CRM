package ru.crm.system.http.handler;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RestErrorResponse(List<String> messages,
                                HttpStatus status,
                                LocalDateTime thrownAt) {
}