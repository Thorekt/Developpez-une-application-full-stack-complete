package com.thorekt.mdd.microservice.user_service.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.thorekt.mdd.microservice.user_service.exception.BadCredentialsException;
import com.thorekt.mdd.microservice.user_service.model.User;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

/**
 * Service for authentication management
 * 
 * @author thorekt
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    // Used for JWT generation
    private final JWTService jwtService;

    // Used for user management
    private final UserService userService;

    // Used for authentication management
    private final AuthenticationManager authenticationManager;

    public String authenticate(String emailOrUsername, String password) throws BadCredentialsException {
        User user;
        Authentication authentication;

        try {
            user = userService.findByEmailOrUsername(emailOrUsername);
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), password));
        } catch (Exception e) {
            throw new BadCredentialsException();
        }

        String jwt = jwtService.generateToken(authentication, user.getId());
        return jwt;
    }

}
