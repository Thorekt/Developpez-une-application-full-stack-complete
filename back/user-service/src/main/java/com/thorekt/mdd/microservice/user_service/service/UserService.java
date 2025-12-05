package com.thorekt.mdd.microservice.user_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.thorekt.mdd.microservice.user_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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

}
