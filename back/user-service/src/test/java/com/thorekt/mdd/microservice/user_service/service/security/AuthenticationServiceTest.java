package com.thorekt.mdd.microservice.user_service.service.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.thorekt.mdd.microservice.user_service.exception.BadCredentialsException;
import com.thorekt.mdd.microservice.user_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.service.business.UserService;
import com.thorekt.mdd.microservice.user_service.service.security.token.ITokenGeneratorService;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    ITokenGeneratorService mockTokenGeneratorService;

    @Mock
    AuthenticationManager mockAuthenticationManager;

    @Mock
    UserService mockUserService;

    @Mock
    Authentication mockAuthentication;

    AuthenticationService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new AuthenticationService(mockTokenGeneratorService, mockUserService,
                mockAuthenticationManager);

    }

    @Test
    public void authenticate_ShouldAuthenticateUser_thenReturnAuthenticationToken() {
        // Given
        String fakeJwt = "dummy-jwt-token";
        String userNameString = "username";
        UUID userUUID = UUID.randomUUID();
        User user = User.builder()
                .uuid(userUUID)
                .username(userNameString)
                .email("user@example.com")
                .password("hashed-password")
                .build();

        Mockito.when(mockUserService.findByEmailOrUsername(userNameString))
                .thenReturn(user);
        Mockito.when(mockAuthenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        Mockito.when(mockTokenGeneratorService.generateToken(mockAuthentication, userUUID))
                .thenReturn(fakeJwt);
        // When
        String jwt = classUnderTest.authenticate(userNameString, "plain-password");

        // Then
        Mockito.verify(mockUserService).findByEmailOrUsername(userNameString);
        Mockito.verify(mockAuthenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(mockTokenGeneratorService).generateToken(mockAuthentication, userUUID);
        assertEquals(fakeJwt, jwt);
    }

    @Test
    public void authenticate_ShouldThrowBadCredentialsException_whenWrongUsernameOrEmail() {
        // Given
        String userNameString = "WrongUsername";

        Mockito.when(mockUserService.findByEmailOrUsername(userNameString))
                .thenThrow(new NotFoundException());

        // When / Then
        String jwt = null;
        try {
            jwt = classUnderTest.authenticate(userNameString, "plain-password");
        } catch (Exception e) {
            assertEquals(BadCredentialsException.class, e.getClass());
        }

        // Then
        assertEquals(null, jwt);
        Mockito.verify(mockUserService).findByEmailOrUsername(userNameString);
        Mockito.verifyNoInteractions(mockAuthenticationManager);
        Mockito.verifyNoInteractions(mockTokenGeneratorService);
    }

    @Test
    public void authenticate_ShouldThrowBadCredentialsException_whenWrongPassword() {
        // Given
        String userNameString = "username";
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .username(userNameString)
                .email("user@example.com")
                .password("hashed-password")
                .build();

        Mockito.when(mockUserService.findByEmailOrUsername(userNameString))
                .thenReturn(user);
        Mockito.when(mockAuthenticationManager.authenticate(Mockito.any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        // When / Then
        String jwt = null;
        try {
            jwt = classUnderTest.authenticate(userNameString, "wrong-password");
        } catch (Exception e) {
            assertEquals(BadCredentialsException.class, e.getClass());
        }

        // Then
        assertEquals(null, jwt);
        Mockito.verify(mockUserService).findByEmailOrUsername(userNameString);
        Mockito.verify(mockAuthenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verifyNoInteractions(mockTokenGeneratorService);

    }

}
