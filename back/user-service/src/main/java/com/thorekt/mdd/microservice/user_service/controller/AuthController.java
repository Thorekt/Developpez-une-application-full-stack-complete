package com.thorekt.mdd.microservice.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thorekt.mdd.microservice.user_service.dto.request.LoginRequest;
import com.thorekt.mdd.microservice.user_service.dto.request.RegisterRequest;
import com.thorekt.mdd.microservice.user_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.user_service.dto.response.AuthResponse;
import com.thorekt.mdd.microservice.user_service.dto.response.ErrorResponse;
import com.thorekt.mdd.microservice.user_service.exception.BadCredentialsException;
import com.thorekt.mdd.microservice.user_service.exception.registration.RegistrationException;
import com.thorekt.mdd.microservice.user_service.service.AuthenticationService;
import com.thorekt.mdd.microservice.user_service.service.RegistrationService;
import com.thorekt.mdd.microservice.user_service.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for authentication and registration operations
 * 
 * @author Thorekt
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    private final RegistrationService registrationService;

    private final UserService userService;

    /**
     * Login a user
     * 
     * @param request
     * @return AuthResponse with JWT token if successful
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginRequest request) {

        try {
            String token = authenticationService.authenticate(
                    request.login(),
                    request.password());

            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

    /**
     * Register a new user
     * 
     * @param request
     * @return AuthResponse with JWT token if successful
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid RegisterRequest request) {
        try {
            registrationService.registerUser(request.email(), request.username(), request.password());
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
        return this.login(new LoginRequest(request.username(), request.password()));
    }

}
