package com.thorekt.mdd.microservice.user_service.service.business.password;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PasswordValidatorServiceTest {

    PasswordValidatorService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new PasswordValidatorService();
    }

    @Test
    public void isValid_ShouldReturnTrueForValidPassword() {
        // Given
        String validPassword = "ValidPass123!";

        // When
        boolean result = classUnderTest.isValid(validPassword);

        // Then
        assertTrue(result);
    }

    @Test
    public void isValid_ShouldReturnFalseForTooShortPassword() {
        // Given
        String shortPassword = "V1d!";
        // When
        boolean result = classUnderTest.isValid(shortPassword);
        // Then
        assertFalse(result);
    }

    @Test
    public void isValid_ShouldReturnFalseForTooLongPassword() {
        // Given
        String longPassword = "V".repeat(65) + "1d!";
        // When
        boolean result = classUnderTest.isValid(longPassword);
        // Then
        assertFalse(result);
    }

    @Test
    public void isValid_ShouldReturnFalseForPasswordMissingUppercase() {
        // Given
        String password = "unvalidpass123!";

        // When
        boolean result = classUnderTest.isValid(password);

        // Then
        assertFalse(result);
    }

    @Test
    public void isValid_ShouldReturnFalseForPasswordMissingLowercase() {
        // Given
        String password = "UNVALIDPASS123!";

        // When
        boolean result = classUnderTest.isValid(password);

        // Then
        assertFalse(result);
    }

}
