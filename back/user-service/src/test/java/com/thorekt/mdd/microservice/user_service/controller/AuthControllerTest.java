package com.thorekt.mdd.microservice.user_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import com.thorekt.mdd.microservice.user_service.dto.UserDto;
import com.thorekt.mdd.microservice.user_service.dto.request.LoginRequest;
import com.thorekt.mdd.microservice.user_service.dto.request.RegisterRequest;
import com.thorekt.mdd.microservice.user_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.user_service.dto.response.AuthResponse;
import com.thorekt.mdd.microservice.user_service.dto.response.ErrorResponse;
import com.thorekt.mdd.microservice.user_service.exception.BadCredentialsException;
import com.thorekt.mdd.microservice.user_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.user_service.exception.registration.EmailAlreadyInUseException;
import com.thorekt.mdd.microservice.user_service.mapper.UserMapper;
import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.service.AuthenticationService;
import com.thorekt.mdd.microservice.user_service.service.RegistrationService;
import com.thorekt.mdd.microservice.user_service.service.UserService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private AuthenticationService mockAuthenticationService;

    @Mock
    private RegistrationService mockRegistrationService;

    @Mock
    private UserService mockUserService;

    @Mock
    private UserMapper mockUserMapper;

    private AuthController classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new AuthController(mockAuthenticationService, mockRegistrationService, mockUserService,
                mockUserMapper);
    }

    @Test
    public void login_ShouldReturnAuthResponse() {
        // Given
        LoginRequest request = new LoginRequest("login", "password");
        String token = "sample-jwt-token";

        Mockito.when(mockAuthenticationService.authenticate(request.login(), request.password()))
                .thenReturn(token);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.login(request);

        // Then
        assertEquals(token, ((AuthResponse) response.getBody()).token());
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        Mockito.verify(mockAuthenticationService).authenticate(request.login(), request.password());
    }

    @Test
    public void login_ShouldReturnAnUnauthorizedResponse_WhenBadCredentials() {
        // Given
        LoginRequest request = new LoginRequest("login", "wrongPassword");

        Mockito.when(mockAuthenticationService.authenticate(request.login(), request.password()))
                .thenThrow(new BadCredentialsException());
        // When
        ResponseEntity<ApiResponse> response = classUnderTest.login(request);

        // Then
        assertEquals(HttpStatusCode.valueOf(401), response.getStatusCode());
        assertEquals("BAD_CREDENTIALS", ((ErrorResponse) response.getBody()).error());
        Mockito.verify(mockAuthenticationService).authenticate(request.login(), request.password());
    }

    @Test
    public void login_ShouldReturnAnInternalServerErrorResponse_WhenExceptionIsThrown() {
        // Given
        LoginRequest request = new LoginRequest("login", "password");

        Mockito.when(mockAuthenticationService.authenticate(request.login(), request.password()))
                .thenThrow(new RuntimeException());

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.login(request);

        // Then
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("INTERNAL_SERVER_ERROR", ((ErrorResponse) response.getBody()).error());
        Mockito.verify(mockAuthenticationService).authenticate(request.login(), request.password());
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
        ResponseEntity<ApiResponse> response = classUnderTest.register(request);

        // Then
        assertEquals(token, ((AuthResponse) response.getBody()).token());
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
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
        ResponseEntity<ApiResponse> response = classUnderTest.register(request);

        // Then
        assertEquals(HttpStatusCode.valueOf(409), response.getStatusCode());
        assertEquals("EMAIL_ALREADY_IN_USE", ((ErrorResponse) response.getBody()).error());
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
        ResponseEntity<ApiResponse> response = classUnderTest.register(request);

        // Then
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("INTERNAL_SERVER_ERROR", ((ErrorResponse) response.getBody()).error());
        Mockito.verify(mockRegistrationService).registerUser(request.email(), request.username(), request.password());
        Mockito.verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void me_ShouldReturnUserDto() {
        // Given
        UUID tokenSub = UUID.randomUUID();
        Jwt jwt = Mockito.mock(Jwt.class);

        Mockito.when(jwt.getClaimAsString("sub")).thenReturn(tokenSub.toString());
        User user = User.builder()
                .uuid(tokenSub)
                .username("username")
                .email("email@example.com")
                .password("password")
                .build();

        UserDto userDto = UserDto.builder()
                .uuid(tokenSub)
                .username("username")
                .email("email@example.com")
                .build();

        Mockito.when(mockUserService.findByUuid(tokenSub.toString())).thenReturn(user);
        Mockito.when(mockUserMapper.toDto(user)).thenReturn(userDto);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.me(jwt);

        // Then
        assertEquals(userDto, ((UserDto) response.getBody()));
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Mockito.verify(mockUserService).findByUuid(tokenSub.toString());
        Mockito.verify(mockUserMapper).toDto(user);
    }

    @Test
    public void me_ShouldReturnNotFoundResponse_WhenUserNotFound() {
        // Given
        String tokenSub = "user-uuid";
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getClaimAsString("sub")).thenReturn(tokenSub);
        Mockito.when(mockUserService.findByUuid(tokenSub)).thenThrow(new NotFoundException());

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.me(jwt);

        // Then
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertEquals("RESOURCE_NOT_FOUND", ((ErrorResponse) response.getBody()).error());
        Mockito.verify(mockUserService).findByUuid(tokenSub);
        Mockito.verifyNoInteractions(mockUserMapper);
    }

    @Test
    public void me_ShouldReturnBadRequestResponse_WhenIllegalArgumentExceptionIsThrown() {
        // Given
        String tokenSub = "user-uuid";
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getClaimAsString("sub")).thenReturn(tokenSub);
        Mockito.when(mockUserService.findByUuid(tokenSub)).thenThrow(new IllegalArgumentException());

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.me(jwt);
        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals("INVALID_FORMAT", ((ErrorResponse) response.getBody()).error());
        Mockito.verify(mockUserService).findByUuid(tokenSub);
        Mockito.verifyNoInteractions(mockUserMapper);
    }

    @Test
    public void me_ShouldReturnInternalServerErrorResponse_WhenExceptionIsThrown() {
        // Given
        String tokenSub = "user-uuid";
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getClaimAsString("sub")).thenReturn(tokenSub);
        Mockito.when(mockUserService.findByUuid(tokenSub)).thenThrow(new RuntimeException());

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.me(jwt);

        // Then
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("INTERNAL_SERVER_ERROR", ((ErrorResponse) response.getBody()).error());
        Mockito.verify(mockUserService).findByUuid(tokenSub);
        Mockito.verifyNoInteractions(mockUserMapper);
    }
}