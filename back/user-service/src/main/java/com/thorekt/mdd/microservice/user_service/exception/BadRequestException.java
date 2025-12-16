package com.thorekt.mdd.microservice.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown for bad request errors
 * 
 * @author thorekt
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    /**
     * Constructor for BadRequestException
     * it sets a default message indicating invalid format.
     */
    public BadRequestException() {
        super("INVALID_FORMAT");
    }
}
