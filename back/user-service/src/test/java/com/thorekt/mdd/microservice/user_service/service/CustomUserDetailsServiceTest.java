package com.thorekt.mdd.microservice.user_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new CustomUserDetailsService(userRepository);
    }

    @Test
    public void loadUserByUsername_ShouldLoadUserDetails() {
        // Given
        User user = User.builder()
                .uuid(java.util.UUID.randomUUID())
                .username("testuser")
                .email("testuser@example.com")
                .password("hashed-password")
                .build();

        Mockito.when(userRepository.findByUsername("testuser"))
                .thenReturn(user);

        // When
        UserDetails userDetails = classUnderTest.loadUserByUsername("testuser");

        // Then
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());

        Mockito.verify(userRepository).findByUsername("testuser");
    }

    @Test
    public void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        // Given
        Mockito.when(userRepository.findByUsername("nonexistentuser"))
                .thenReturn(null);

        // When / Then
        try {
            classUnderTest.loadUserByUsername("nonexistentuser");
            assertEquals(true, false); // Force fail if no exception is thrown
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            assertEquals("User not found", e.getMessage());
        }

        Mockito.verify(userRepository).findByUsername("nonexistentuser");
    }
}
