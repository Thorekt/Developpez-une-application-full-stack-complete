package com.thorekt.mdd.microservice.theme_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not found.
 * 
 * @author Thorekt
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    /**
     * Constructs a new NotFoundException with a default message.
     */
    public NotFoundException() {
        super("RESOURCE_NOT_FOUND");
    }
}
