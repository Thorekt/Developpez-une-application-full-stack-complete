package com.thorekt.mdd.microservice.user_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import com.thorekt.mdd.microservice.user_service.dto.request.LoginRequest;
import com.thorekt.mdd.microservice.user_service.dto.request.RegisterRequest;
import com.thorekt.mdd.microservice.user_service.dto.response.AuthResponse;
import com.thorekt.mdd.microservice.user_service.exception.BadCredentialsException;
import com.thorekt.mdd.microservice.user_service.exception.registration.EmailAlreadyInUseException;
import com.thorekt.mdd.microservice.user_service.service.AuthenticationService;
import com.thorekt.mdd.microservice.user_service.service.RegistrationService;
import com.thorekt.mdd.microservice.user_service.service.UserService;

import io.micrometer.core.ipc.http.HttpSender.Response;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private AuthenticationService mockAuthenticationService;

    @Mock
    private RegistrationService mockRegistrationService;

    @Mock
    private UserService mockUserService;

    private AuthController classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new AuthController(mockAuthenticationService, mockRegistrationService, mockUserService);
    }

    @Test
    public void login_ShouldReturnAuthResponse() {
        // Given
        LoginRequest request = new LoginRequest("loginValue", "password");
        String token = "sample-jwt-token";

        Mockito.when(mockAuthenticationService.authenticate(request.loginValue(), request.password()))
                .thenReturn(token);

        // When
        ResponseEntity<AuthResponse> response = classUnderTest.login(request);

        // Then
        assertEquals(token, response.getBody().token());
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertEquals("n/a", response.getBody().error());
        Mockito.verify(mockAuthenticationService).authenticate(request.loginValue(), request.password());
    }

    @Test
    public void login_ShouldReturnAnUnauthorizedResponse_WhenBadCredentials() {
        // Given
        LoginRequest request = new LoginRequest("loginValue", "wrongPassword");

        Mockito.when(mockAuthenticationService.authenticate(request.loginValue(), request.password()))
                .thenThrow(new BadCredentialsException());
        // When
        ResponseEntity<AuthResponse> response = classUnderTest.login(request);

        // Then
        assertEquals(null, response.getBody().token());
        assertEquals(HttpStatusCode.valueOf(401), response.getStatusCode());
        assertEquals("BAD_CREDENTIALS", response.getBody().error());
        Mockito.verify(mockAuthenticationService).authenticate(request.loginValue(), request.password());
    }

    @Test
    public void login_ShouldReturnAnInternalServerErrorResponse_WhenExceptionIsThrown() {
        // Given
        LoginRequest request = new LoginRequest("loginValue", "password");

        Mockito.when(mockAuthenticationService.authenticate(request.loginValue(), request.password()))
                .thenThrow(new RuntimeException());

        // When
        ResponseEntity<AuthResponse> response = classUnderTest.login(request);

        // Then
        assertEquals(null, response.getBody().token());
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().error());
        Mockito.verify(mockAuthenticationService).authenticate(request.loginValue(), request.password());
    }

    @Test
    public void register_ShouldReturnAuthResponse() {
        // Given
        RegisterRequest request = new RegisterRequest("username", "email@example.com", "password");
        String token = "sample-jwt-token";

        Mockito.when(mockAuthenticationService.authenticate(request.username(), request.password()))
                .thenReturn(token);

        Mockito.doNothing().when(mockRegistrationService)
                .registerUser(request.email(), request.username(), request.password());

        // When
        ResponseEntity<AuthResponse> response = classUnderTest.register(request);

        // Then
        assertEquals(token, response.getBody().token());
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertEquals("n/a", response.getBody().error());
        Mockito.verify(mockRegistrationService).registerUser(request.email(), request.username(), request.password());
        Mockito.verify(mockAuthenticationService).authenticate(request.username(), request.password());

    }

    @Test
    public void register_ShouldReturnAConflictResponse_WhenRegistrationExceptionIsThrown() {
        // Given
        RegisterRequest request = new RegisterRequest("username", "email@example.com", "password");

        Mockito.doThrow(new EmailAlreadyInUseException())
                .when(mockRegistrationService).registerUser(request.email(), request.username(), request.password());

        // When
        ResponseEntity<AuthResponse> response = classUnderTest.register(request);

        // Then
        assertEquals("n/a", response.getBody().token());
        assertEquals(HttpStatusCode.valueOf(409), response.getStatusCode());
        assertEquals("EMAIL_ALREADY_IN_USE", response.getBody().error());
        Mockito.verify(mockRegistrationService).registerUser(request.email(), request.username(), request.password());
        Mockito.verifyNoInteractions(mockAuthenticationService);

    }

    @Test
    public void register_ShouldReturnAnInternalServerErrorResponse_WhenExceptionIsThrown() {
        // Given
        RegisterRequest request = new RegisterRequest("username", "email@example.com", "password");

        Mockito.doThrow(new RuntimeException())
                .when(mockRegistrationService).registerUser(request.email(), request.username(), request.password());

        // When
        ResponseEntity<AuthResponse> response = classUnderTest.register(request);

        // Then
        assertEquals("n/a", response.getBody().token());
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().error());
        Mockito.verify(mockRegistrationService).registerUser(request.email(), request.username(), request.password());
        Mockito.verifyNoInteractions(mockAuthenticationService);

    }
}