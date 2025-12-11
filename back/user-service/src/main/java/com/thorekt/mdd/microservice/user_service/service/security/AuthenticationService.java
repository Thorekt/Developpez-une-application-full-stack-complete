package com.thorekt.mdd.microservice.user_service.service.security;

import org.aspectj.weaver.patterns.IToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.thorekt.mdd.microservice.user_service.exception.BadCredentialsException;
import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.service.business.UserService;
import com.thorekt.mdd.microservice.user_service.service.security.token.ITokenGeneratorService;
import com.thorekt.mdd.microservice.user_service.service.security.token.JWTGeneratorService;

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
    private final ITokenGeneratorService tokenGeneratorService;

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
                    new UsernamePasswordAuthenticationToken(user.getUsername(), password));
        } catch (Exception e) {
            throw new BadCredentialsException();
        }

        String jwt = tokenGeneratorService.generateToken(authentication, user.getUuid());
        return jwt;
    }

}
