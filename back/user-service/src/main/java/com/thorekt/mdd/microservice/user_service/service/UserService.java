package com.thorekt.mdd.microservice.user_service.service;

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
}
