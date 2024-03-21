package ru.crm.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CreateEntityException extends ResponseStatusException {

    public CreateEntityException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}