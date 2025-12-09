package com.thorekt.mdd.microservice.theme_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thorekt.mdd.microservice.theme_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.theme_service.model.Theme;
import com.thorekt.mdd.microservice.theme_service.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
public class ThemeServiceTest {
    @Mock
    ThemeRepository mockThemeRepository;

    ThemeService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new ThemeService(mockThemeRepository);
    }

    @Test
    public void findAllThemes_ShouldInvokeRepositoryMethod() {
        // Given

        // When
        classUnderTest.findAllThemes();

        // Then
        Mockito.verify(mockThemeRepository).findAll();

    }

    @Test
    public void findByUuid_ShouldInvokeRepositoryMethod() {
        // Given
        UUID uuidString = UUID.randomUUID();
        String uuid = uuidString.toString();
        Theme theme = Theme.builder()
                .uuid(uuidString)
                .title("Test Theme")
                .description("This is a test theme")
                .build();

        Mockito.when(mockThemeRepository.findByUuid(uuidString))
                .thenReturn(theme);

        // When
        Theme foundTheme = classUnderTest.findByUuid(uuid);

        // Then
        Mockito.verify(mockThemeRepository).findByUuid(uuidString);
        assertEquals(theme, foundTheme);
    }

    @Test
    public void findByUuid_ShouldThrowNotFoundException_whenThemeDoesNotExist() {
        // Given
        UUID uuidString = UUID.randomUUID();
        String uuid = uuidString.toString();
        Mockito.when(mockThemeRepository.findByUuid(uuidString))
                .thenReturn(null);

        // When / Then
        Theme theme = null;
        try {
            theme = classUnderTest.findByUuid(uuid);
            assertEquals(true, false); // Force fail if no exception is thrown
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass());
        }

        // Then
        Mockito.verify(mockThemeRepository).findByUuid(uuidString);
        assertEquals(theme, null);
    }
}