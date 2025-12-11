package com.thorekt.mdd.microservice.user_service.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thorekt.mdd.microservice.user_service.exception.registration.EmailAlreadyInUseException;
import com.thorekt.mdd.microservice.user_service.exception.registration.EmailAndUsernameAlreadyInUseException;
import com.thorekt.mdd.microservice.user_service.exception.registration.UsernameAlreadyInUseException;
import com.thorekt.mdd.microservice.user_service.repository.UserRepository;
import com.thorekt.mdd.microservice.user_service.service.business.RegistrationService;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    RegistrationService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new RegistrationService(mockUserRepository, mockPasswordEncoder);
    }

    @Test
    void testRegisterUser_ShouldRegisterSuccessfully() {
        // Given
        String email = "test@example.com";
        String username = "testuser";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        Mockito.when(mockUserRepository.existsByEmail(email)).thenReturn(false);
        Mockito.when(mockUserRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(mockPasswordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        Mockito.when(mockUserRepository.save(Mockito.any())).thenAnswer(i -> i.getArguments()[0]);

        // When
        try {
            classUnderTest.registerUser(email, username, rawPassword);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }

        // Then
        Mockito.verify(mockUserRepository).existsByEmail(email);
        Mockito.verify(mockUserRepository).existsByUsername(username);
        Mockito.verify(mockPasswordEncoder).encode(rawPassword);
        Mockito.verify(mockUserRepository).save(Mockito.any());
    }

    @Test
    void testRegisterUser_ShouldThrowEmailAlreadyInUseException() {
        // Given
        String email = "test@example.com";
        String username = "testuser";
        String rawPassword = "password123";

        Mockito.when(mockUserRepository.existsByEmail(email)).thenReturn(true);
        Mockito.when(mockUserRepository.existsByUsername(username)).thenReturn(false);

        // When / Then
        try {
            classUnderTest.registerUser(email, username, rawPassword);
            assert false : "EmailAlreadyInUseException should have been thrown";
        } catch (Exception e) {
            assertEquals(EmailAlreadyInUseException.class, e.getClass());
        }

        Mockito.verify(mockUserRepository).existsByEmail(email);
        Mockito.verify(mockUserRepository).existsByUsername(username);
    }

    @Test
    void testRegisterUser_ShouldThrowUsernameAlreadyInUseException() {
        // Given
        String email = "test@example.com";
        String username = "testuser";
        String rawPassword = "password123";

        Mockito.when(mockUserRepository.existsByEmail(email)).thenReturn(false);
        Mockito.when(mockUserRepository.existsByUsername(username)).thenReturn(true);

        // When / Then
        try {
            classUnderTest.registerUser(email, username, rawPassword);
            assert false : "UsernameAlreadyInUseException should have been thrown";
        } catch (Exception e) {
            assertEquals(UsernameAlreadyInUseException.class, e.getClass());
        }

        Mockito.verify(mockUserRepository).existsByEmail(email);
        Mockito.verify(mockUserRepository).existsByUsername(username);
    }

    @Test
    void testRegisterUser_ShouldThrowEmailAndUsernameAlreadyInUseException() {
        // Given
        String email = "test@example.com";
        String username = "testuser";
        String rawPassword = "password123";

        Mockito.when(mockUserRepository.existsByEmail(email)).thenReturn(true);
        Mockito.when(mockUserRepository.existsByUsername(username)).thenReturn(true);

        // When / Then
        try {
            classUnderTest.registerUser(email, username, rawPassword);
            assert false : "EmailAndUsernameAlreadyInUseException should have been thrown";
        } catch (Exception e) {
            assertEquals(EmailAndUsernameAlreadyInUseException.class, e.getClass());
        }

        Mockito.verify(mockUserRepository).existsByEmail(email);
        Mockito.verify(mockUserRepository).existsByUsername(username);

    }
}