package com.thorekt.mdd.microservice.user_service.exception.registration;

/**
 * Base exception for registration-related errors
 * 
 * @author thorekt
 */
public class RegistrationException extends RuntimeException {

    /**
     * Constructor for RegistrationException
     * 
     * @param message Error message
     */
    public RegistrationException(String message) {
        super(message);
    }
}
