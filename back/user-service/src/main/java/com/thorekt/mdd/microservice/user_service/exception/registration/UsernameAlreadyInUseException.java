package com.thorekt.mdd.microservice.user_service.exception.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UsernameAlreadyInUseException extends RuntimeException {
}
