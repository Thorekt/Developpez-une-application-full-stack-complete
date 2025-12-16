package com.thorekt.mdd.microservice.user_service.exception.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when both email and username are already in use during
 * registration
 * 
 * @author thorekt
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class EmailAndUsernameAlreadyInUseException extends RegistrationException {
    /**
     * Constructor for EmailAndUsernameAlreadyInUseException
     * it sets a default message indicating both email and username are already in
     * use.
     */
    public EmailAndUsernameAlreadyInUseException() {
        super("EMAIL_AND_USERNAME_ALREADY_IN_USE");
    }
}
