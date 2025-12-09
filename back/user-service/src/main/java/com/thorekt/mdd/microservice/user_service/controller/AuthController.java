package com.thorekt.mdd.microservice.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thorekt.mdd.microservice.user_service.dto.UserDto;
import com.thorekt.mdd.microservice.user_service.dto.request.LoginRequest;
import com.thorekt.mdd.microservice.user_service.dto.request.RegisterRequest;
import com.thorekt.mdd.microservice.user_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.user_service.dto.response.AuthResponse;
import com.thorekt.mdd.microservice.user_service.dto.response.ErrorResponse;
import com.thorekt.mdd.microservice.user_service.exception.BadCredentialsException;
import com.thorekt.mdd.microservice.user_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.user_service.exception.registration.RegistrationException;
import com.thorekt.mdd.microservice.user_service.mapper.UserMapper;
import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.service.AuthenticationService;
import com.thorekt.mdd.microservice.user_service.service.RegistrationService;
import com.thorekt.mdd.microservice.user_service.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

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

    private final UserMapper userMapper;

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

    /**
     * Get the current authenticated user's details
     * 
     * @param user uuid
     * @return User details
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> me(@AuthenticationPrincipal Jwt jwt) {
        String uuid = jwt.getClaimAsString("sub");

        try {
            User user = userService.findByUuid(uuid);
            UserDto userDto = userMapper.toDto(user);
            return ResponseEntity.ok(userDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

}
