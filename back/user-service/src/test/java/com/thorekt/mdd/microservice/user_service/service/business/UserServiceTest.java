package com.thorekt.mdd.microservice.user_service.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thorekt.mdd.microservice.user_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.repository.UserRepository;
import com.thorekt.mdd.microservice.user_service.service.business.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository mockUserRepository;
    @Mock
    PasswordEncoder mockPasswordEncoder;

    UserService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new UserService(mockUserRepository, mockPasswordEncoder);
    }

    @Test
    public void findByEmailOrUsername_usingEmail_ShouldReturnUser_whenUserExists() {
        // Given
        String emailString = "test-user@example.com";
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .username("test-user")
                .email(emailString)
                .password("hashed-password")
                .build();

        Mockito.when(mockUserRepository.findByEmailOrUsername(emailString, emailString))
                .thenReturn(user);

        // When
        User foundUser = classUnderTest.findByEmailOrUsername(emailString);

        // Then
        assertEquals(user, foundUser);
        Mockito.verify(mockUserRepository).findByEmailOrUsername(emailString, emailString);
    }

    @Test
    public void findByEmailOrUsername_usingUsername_ShouldReturnUser_whenUserExists() {
        // Given
        String usernameString = "test-user";
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .username(usernameString)
                .email("test-user@example.com")
                .password("hashed-password")
                .build();

        Mockito.when(mockUserRepository.findByEmailOrUsername(usernameString, usernameString))
                .thenReturn(user);

        // When
        User foundUser = classUnderTest.findByEmailOrUsername(usernameString);

        // Then
        assertEquals(user, foundUser);
        Mockito.verify(mockUserRepository).findByEmailOrUsername(usernameString, usernameString);
    }

    @Test
    public void findByEmailOrUsername_ShouldThrowNotFoundException_whenUserDoesNotExist() {
        // Given
        String emailOrUsername = "nonexistent-user";
        Mockito.when(mockUserRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername))
                .thenReturn(null);
        // When / Then
        User foundUser = null;
        try {
            foundUser = classUnderTest.findByEmailOrUsername(emailOrUsername);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass());
        }
        assertNull(foundUser);
        Mockito.verify(mockUserRepository).findByEmailOrUsername(emailOrUsername, emailOrUsername);
    }

    @Test
    public void findByUuid_ShouldReturnUser_whenUserExists() {
        // Given
        UUID userUuid = UUID.randomUUID();
        User user = User.builder()
                .uuid(userUuid)
                .username("test-user")
                .email("test-user@example.com")
                .password("hashed-password")
                .build();

        Mockito.when(mockUserRepository.findByUuid(userUuid))
                .thenReturn(user);

        // When
        User foundUser = classUnderTest.findByUuid(userUuid.toString());

        // Then
        assertEquals(user, foundUser);
        Mockito.verify(mockUserRepository).findByUuid(userUuid);
    }

    @Test
    public void findByUuid_ShouldThrowNotFoundException_whenUserDoesNotExist() {
        // Given
        UUID userUuid = UUID.randomUUID();
        Mockito.when(mockUserRepository.findByUuid(userUuid))
                .thenReturn(null);
        // When / Then
        User foundUser = null;
        try {
            foundUser = classUnderTest.findByUuid(userUuid.toString());
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass());
        }
        assertNull(foundUser);
        Mockito.verify(mockUserRepository).findByUuid(userUuid);
    }

    @Test
    public void updateUser_ShouldUpdateUserDetails_whenUserExists() {
        // Given
        String userUuidString = UUID.randomUUID().toString();
        UUID userUuid = UUID.fromString(userUuidString);
        User existingUser = User.builder()
                .uuid(userUuid)
                .username("old-username")
                .email("old-email@example.com")
                .password("old-password")
                .build();

        String newEmail = "new-email@example.com";
        String newUsername = "new-username";
        String newPassword = "new-password";
        String encodedNewPassword = "encoded-new-password";

        Mockito.when(mockPasswordEncoder.encode(newPassword))
                .thenReturn(encodedNewPassword);

        Mockito.when(mockUserRepository.findByUuid(userUuid))
                .thenReturn(existingUser);

        // When
        classUnderTest.updateUser(userUuidString, newEmail, newUsername, newPassword);

        // Then
        assertEquals(newEmail, existingUser.getEmail());
        assertEquals(newUsername, existingUser.getUsername());
        assertEquals(encodedNewPassword, existingUser.getPassword());
        Mockito.verify(mockUserRepository).findByUuid(userUuid);
        Mockito.verify(mockUserRepository).save(existingUser);
    }

    @Test
    public void updateUser_ShouldThrowNotFoundException_whenUserDoesNotExist() {
        // Given
        String userUuidString = UUID.randomUUID().toString();
        UUID userUuid = UUID.fromString(userUuidString);
        Mockito.when(mockUserRepository.findByUuid(userUuid))
                .thenReturn(null);

        // When / Then
        try {
            classUnderTest.updateUser(userUuidString, "email", "username", "password");
            assertEquals(true, false); // Force fail if no exception is thrown
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass());
        }
        Mockito.verify(mockUserRepository).findByUuid(userUuid);
        Mockito.verifyNoMoreInteractions(mockUserRepository);
        Mockito.verifyNoInteractions(mockPasswordEncoder);
    }
}