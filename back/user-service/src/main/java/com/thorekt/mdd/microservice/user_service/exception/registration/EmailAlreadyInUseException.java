package com.thorekt.mdd.microservice.user_service.exception.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class EmailAlreadyInUseException extends RegistrationException {
    public EmailAlreadyInUseException() {
        super("EMAIL_ALREADY_IN_USE");
    }
}
