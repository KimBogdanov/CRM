package ru.crm.system.http.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.crm.system.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestControllerAdvice(basePackages = "ru.crm.system.http")
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestErrorResponse> handleNotFoundException(NotFoundException exception) {
        return createErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<RestErrorResponse> createErrorResponse(Exception exception, HttpStatus status) {
        return new ResponseEntity<>(RestErrorResponse.builder()
                .message(exception.getMessage())
                .status(status)
                .thrownAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build(),
                status);
    }

}
