package com.thorekt.mdd.microservice.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when provided credentials are invalid
 * 
 * @author thorekt
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class BadCredentialsException extends RuntimeException {
    /**
     * Constructor for BadCredentialsException
     * it sets a default message indicating bad credentials.
     */
    public BadCredentialsException() {
        super("BAD_CREDENTIALS");
    }
}
