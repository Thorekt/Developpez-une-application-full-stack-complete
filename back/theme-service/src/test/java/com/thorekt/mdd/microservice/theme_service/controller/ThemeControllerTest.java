package com.thorekt.mdd.microservice.theme_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.thorekt.mdd.microservice.theme_service.dto.ThemeDto;
import com.thorekt.mdd.microservice.theme_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.theme_service.dto.response.ThemeListResponse;
import com.thorekt.mdd.microservice.theme_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.theme_service.mapper.ThemeMapper;
import com.thorekt.mdd.microservice.theme_service.model.Theme;
import com.thorekt.mdd.microservice.theme_service.service.ThemeService;

@ExtendWith(MockitoExtension.class)
public class ThemeControllerTest {
    @Mock
    ThemeService themeService;

    @Mock
    ThemeMapper themeMapper;

    ThemeController classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new ThemeController(themeService, themeMapper);
    }

    @Test
    public void findAllThemes_ShouldReturnListOfThemes() {
        // Given
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        Theme theme1 = Theme.builder().uuid(uuid1).title("Theme 1").description("some description").build();
        Theme theme2 = Theme.builder().uuid(uuid2).title("Theme 2").description("some description").build();
        List<Theme> themes = List.of(theme1, theme2);

        ThemeDto themeDto1 = new ThemeDto(uuid1, "Theme 1", "some description");
        ThemeDto themeDto2 = new ThemeDto(uuid2, "Theme 2", "some description");

        Mockito.when(themeService.findAllThemes()).thenReturn(themes);
        Mockito.when(themeMapper.toDto(theme1)).thenReturn(themeDto1);
        Mockito.when(themeMapper.toDto(theme2)).thenReturn(themeDto2);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.findAllThemes();

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        ThemeListResponse body = (ThemeListResponse) response.getBody();
        assertTrue(body.themes().size() == 2);
        List<ThemeDto> themeDtos = body.themes();
        assertEquals("Theme 1", themeDtos.get(0).getTitle());
        assertEquals("Theme 2", themeDtos.get(1).getTitle());
        Mockito.verify(themeService).findAllThemes();
        Mockito.verify(themeMapper).toDto(theme1);
        Mockito.verify(themeMapper).toDto(theme2);
    }

    @Test
    public void findAllThemes_ShouldHandleException() {
        // Given
        Mockito.when(themeService.findAllThemes()).thenThrow(new RuntimeException("Database error"));
        // When
        ResponseEntity<ApiResponse> response = classUnderTest.findAllThemes();
        // Then
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Mockito.verify(themeService).findAllThemes();
        Mockito.verifyNoInteractions(themeMapper);
    }

    @Test
    public void findThemeByUuid_ShouldReturnTheme() throws Exception {
        // Given
        UUID uuid = UUID.randomUUID();
        Theme theme = Theme.builder().uuid(uuid).title("Theme 1").description("some description").build();
        ThemeDto themeDto = new ThemeDto(uuid, "Theme 1", "some description");

        Mockito.when(themeService.findByUuid(uuid.toString())).thenReturn(theme);
        Mockito.when(themeMapper.toDto(theme)).thenReturn(themeDto);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.findThemeByUuid(uuid.toString());

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        ThemeDto body = (ThemeDto) response.getBody();
        assertEquals("Theme 1", body.getTitle());
        Mockito.verify(themeService).findByUuid(uuid.toString());
        Mockito.verify(themeMapper).toDto(theme);
    }

    @Test
    public void findThemeByUuid_ShouldHandleNotFoundException() throws Exception {
        // Given
        UUID uuid = UUID.randomUUID();
        Mockito.when(themeService.findByUuid(uuid.toString()))
                .thenThrow(NotFoundException.class);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.findThemeByUuid(uuid.toString());

        // Then
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        Mockito.verify(themeService).findByUuid(uuid.toString());
        Mockito.verifyNoInteractions(themeMapper);
    }

    @Test
    public void findThemeByUuid_ShouldHandleIllegalArgumentException() throws Exception {
        // Given
        String invalidUuid = UUID.randomUUID().toString();
        Mockito.when(themeService.findByUuid(invalidUuid))
                .thenThrow(IllegalArgumentException.class);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.findThemeByUuid(invalidUuid);
        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Mockito.verify(themeService).findByUuid(invalidUuid);
        Mockito.verifyNoInteractions(themeMapper);
    }

    @Test
    public void findThemeByUuid_ShouldHandleGenericException() throws Exception {
        // Given
        UUID uuid = UUID.randomUUID();
        Mockito.when(themeService.findByUuid(uuid.toString()))
                .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.findThemeByUuid(uuid.toString());

        // Then
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Mockito.verify(themeService).findByUuid(uuid.toString());
        Mockito.verifyNoInteractions(themeMapper);
    }
}
