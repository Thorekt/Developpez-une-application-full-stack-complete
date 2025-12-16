package com.thorekt.mdd.microservice.user_service.service.business.password;

/**
 * Service interface for validating passwords
 * 
 * @author thorekt
 */
public interface IPasswordValidatorService {
    /**
     * Validate the given password against defined criteria
     * 
     * @param password Password to validate
     * @return true if the password is valid, false otherwise
     */
    public boolean isValid(String password);
}
