package com.thorekt.mdd.microservice.user_service.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thorekt.mdd.microservice.user_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

/**
 * Service for user operations
 * 
 * @author Thorekt
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Find a user by email or username
     * 
     * @param emailOrUsername Email or username of the user
     * @return the found User
     * @throws NotFoundException
     */
    public User findByEmailOrUsername(String emailOrUsername) throws NotFoundException {
        User user = userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername);
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    /**
     * Find a user by uuid
     * 
     * @param uuid UUID of the user
     * @return the found User
     * @throws NotFoundException
     */
    public User findByUuid(String uuid) throws NotFoundException {
        User user = userRepository.findByUuid(UUID.fromString(uuid));
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    @Transactional
    public void updateUser(String uuid, @Email String email, String username, String password)
            throws NotFoundException {
        User user = userRepository.findByUuid(UUID.fromString(uuid));
        if (user == null) {
            throw new NotFoundException();
        }
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

}
