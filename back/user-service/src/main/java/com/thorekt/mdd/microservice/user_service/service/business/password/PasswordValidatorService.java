package com.thorekt.mdd.microservice.user_service.service.business.password;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for password validation
 * 
 * @author Thorekt
 */
@Service
public class PasswordValidatorService implements IPasswordValidatorService {
    @Value("${password.policy.min-length:8}")
    int minLength = 8;

    @Value("${password.policy.max-length:64}")
    int maxLength = 64;

    /**
     * Validate the given password against defined criteria
     * 
     * @param password Password to validate
     * @return true if the password is valid, false otherwise
     */
    @Override
    public boolean isValid(String password) {
        if (password.length() < minLength || password.length() > maxLength) {
            return false;
        }

        return password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d.*")
                && password.matches(".*[-!@#$%^&*(),.?\":{}|<>].*");
    }
}
