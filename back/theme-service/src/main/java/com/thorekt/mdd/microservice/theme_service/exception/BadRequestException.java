package com.thorekt.mdd.microservice.theme_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown for bad requests with invalid format.
 * 
 * @author Thorekt
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    /**
     * Constructs a new BadRequestException with a default message.
     */
    public BadRequestException() {
        super("INVALID_FORMAT");
    }
}
