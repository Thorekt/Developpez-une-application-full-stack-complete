package com.thorekt.mdd.microservice.user_service.exception.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an email is already in use during registration
 * 
 * @author thorekt
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class EmailAlreadyInUseException extends RegistrationException {
    /**
     * Constructor for EmailAlreadyInUseException
     * it sets a default message indicating the email is already in use.
     */
    public EmailAlreadyInUseException() {
        super("EMAIL_ALREADY_IN_USE");
    }
}
