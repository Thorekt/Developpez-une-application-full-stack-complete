package com.thorekt.mdd.microservice.user_service.service.business;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thorekt.mdd.microservice.user_service.exception.registration.EmailAlreadyInUseException;
import com.thorekt.mdd.microservice.user_service.exception.registration.EmailAndUsernameAlreadyInUseException;
import com.thorekt.mdd.microservice.user_service.exception.registration.RegistrationException;
import com.thorekt.mdd.microservice.user_service.exception.registration.UsernameAlreadyInUseException;
import com.thorekt.mdd.microservice.user_service.exception.BadRequestException;
import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.repository.UserRepository;
import com.thorekt.mdd.microservice.user_service.service.business.password.IPasswordValidatorService;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

/**
 * Service for user registration
 * 
 * @author Thorekt
 */
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;

    private final IPasswordValidatorService passwordValidator;

    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user
     * 
     * @param email
     * @param rawPassword
     * @param name
     * @throws Exception
     */
    @Transactional
    public void registerUser(@Email String email, String username, String rawPassword)
            throws RegistrationException, BadRequestException {
        boolean emailExists = userRepository.existsByEmail(email);
        boolean usernameExists = userRepository.existsByUsername(username);
        if (emailExists && usernameExists) {
            throw new EmailAndUsernameAlreadyInUseException();
        } else if (emailExists) {
            throw new EmailAlreadyInUseException();
        } else if (usernameExists) {
            throw new UsernameAlreadyInUseException();
        }

        if (!passwordValidator.isValid(rawPassword)) {
            throw new BadRequestException();
        }

        User newUser = User.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .build();
        userRepository.save(newUser);
    }
}
