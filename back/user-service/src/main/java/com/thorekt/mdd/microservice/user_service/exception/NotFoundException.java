package com.thorekt.mdd.microservice.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not found
 * 
 * @author thorekt
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    /**
     * Constructor for NotFoundException
     * it sets a default message indicating the resource was not found.
     */
    public NotFoundException() {
        super("RESOURCE_NOT_FOUND");
    }
}
