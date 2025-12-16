package com.thorekt.mdd.microservice.user_service.exception.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a username is already in use during registration
 * 
 * @author thorekt
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class UsernameAlreadyInUseException extends RegistrationException {

    /**
     * Constructor for UsernameAlreadyInUseException
     * it sets a default message indicating the username is already in use. *
     */
    public UsernameAlreadyInUseException() {
        super("USERNAME_ALREADY_IN_USE");
    }
}
