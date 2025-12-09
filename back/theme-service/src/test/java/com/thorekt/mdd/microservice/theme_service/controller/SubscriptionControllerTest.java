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
import org.springframework.security.oauth2.jwt.Jwt;

import com.thorekt.mdd.microservice.theme_service.dto.ThemeDto;
import com.thorekt.mdd.microservice.theme_service.dto.request.SubscriptionRequest;
import com.thorekt.mdd.microservice.theme_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.theme_service.dto.response.ThemeListResponse;
import com.thorekt.mdd.microservice.theme_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.theme_service.mapper.ThemeMapper;
import com.thorekt.mdd.microservice.theme_service.model.Subscription;
import com.thorekt.mdd.microservice.theme_service.model.Theme;
import com.thorekt.mdd.microservice.theme_service.service.SubscriptionService;

@ExtendWith(MockitoExtension.class)
public class SubscriptionControllerTest {
    @Mock
    SubscriptionService mockSubscriptionService;

    @Mock
    ThemeMapper mockThemeMapper;

    @Mock
    Jwt mockJwt;

    SubscriptionController classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new SubscriptionController(mockSubscriptionService, mockThemeMapper);
    }

    @Test
    public void findAllSubscribedThemes_ShouldReturnListOfSubscribedThemes() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        UUID themeUuid1 = UUID.randomUUID();
        UUID themeUuid2 = UUID.randomUUID();

        Theme theme1 = Theme.builder().uuid(themeUuid1).title("Theme 1").description("desc 1").build();
        Theme theme2 = Theme.builder().uuid(themeUuid2).title("Theme 2").description("desc 2").build();

        Subscription sub1 = Subscription.builder().uuid(UUID.randomUUID()).theme(theme1)
                .userUuid(UUID.fromString(userUuid)).build();
        Subscription sub2 = Subscription.builder().uuid(UUID.randomUUID()).theme(theme2)
                .userUuid(UUID.fromString(userUuid)).build();

        ThemeDto themeDto1 = new ThemeDto(themeUuid1, "Theme 1", "desc 1");
        ThemeDto themeDto2 = new ThemeDto(themeUuid2, "Theme 2", "desc 2");

        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.when(mockSubscriptionService.findSubscriptionsByUserUuid(userUuid)).thenReturn(List.of(sub1, sub2));
        Mockito.when(mockThemeMapper.toDto(theme1)).thenReturn(themeDto1);
        Mockito.when(mockThemeMapper.toDto(theme2)).thenReturn(themeDto2);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.findAllSubscribedThemes(mockJwt);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        ThemeListResponse body = (ThemeListResponse) response.getBody();
        assertTrue(body.themes().size() == 2);
        assertEquals("Theme 1", body.themes().get(0).getTitle());
        assertEquals("Theme 2", body.themes().get(1).getTitle());
        Mockito.verify(mockSubscriptionService).findSubscriptionsByUserUuid(userUuid);
        Mockito.verify(mockThemeMapper).toDto(theme1);
        Mockito.verify(mockThemeMapper).toDto(theme2);
    }

    @Test
    public void findAllSubscribedThemes_ShouldReturnNotFound_WhenNotFoundExceptionThrown() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.when(mockSubscriptionService.findSubscriptionsByUserUuid(userUuid))
                .thenThrow(new NotFoundException());

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.findAllSubscribedThemes(mockJwt);

        // Then
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).findSubscriptionsByUserUuid(userUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

    @Test
    public void findAllSubscribedThemes_ShouldReturnBadRequest_WhenIllegalArgumentExceptionThrown() {
        // Given
        String userUuid = "invalid-uuid";
        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.when(mockSubscriptionService.findSubscriptionsByUserUuid(userUuid))
                .thenThrow(new IllegalArgumentException("Invalid UUID"));

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.findAllSubscribedThemes(mockJwt);

        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).findSubscriptionsByUserUuid(userUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

    @Test
    public void findAllSubscribedThemes_ShouldReturnInternalServerError_WhenOtherExceptionThrown() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.when(mockSubscriptionService.findSubscriptionsByUserUuid(userUuid))
                .thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.findAllSubscribedThemes(mockJwt);

        // Then
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).findSubscriptionsByUserUuid(userUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

    @Test
    public void subscribeToTheme_ShouldSubscribeUserToTheme() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        String themeUuid = UUID.randomUUID().toString();
        SubscriptionRequest request = new SubscriptionRequest(themeUuid);

        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.doNothing().when(mockSubscriptionService).subscribeToTheme(userUuid, themeUuid);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.subscribeToTheme(mockJwt, request);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).subscribeToTheme(userUuid, themeUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

    @Test
    public void subscribeToTheme_ShouldReturnNotFound_WhenNotFoundExceptionThrown() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        String themeUuid = UUID.randomUUID().toString();
        SubscriptionRequest request = new SubscriptionRequest(themeUuid);

        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.doThrow(new NotFoundException()).when(mockSubscriptionService)
                .subscribeToTheme(userUuid, themeUuid);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.subscribeToTheme(mockJwt, request);

        // Then
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).subscribeToTheme(userUuid, themeUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

    @Test
    public void subscribeToTheme_ShouldReturnBadRequest_WhenIllegalArgumentExceptionThrown() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        String themeUuid = "invalid-uuid";
        SubscriptionRequest request = new SubscriptionRequest(themeUuid);

        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.doThrow(new IllegalArgumentException("Invalid UUID")).when(mockSubscriptionService)
                .subscribeToTheme(userUuid, themeUuid);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.subscribeToTheme(mockJwt, request);

        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).subscribeToTheme(userUuid, themeUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

    @Test
    public void subscribeToTheme_ShouldReturnInternalServerError_WhenOtherExceptionThrown() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        String themeUuid = UUID.randomUUID().toString();
        SubscriptionRequest request = new SubscriptionRequest(themeUuid);

        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.doThrow(new RuntimeException("Database error")).when(mockSubscriptionService)
                .subscribeToTheme(userUuid, themeUuid);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.subscribeToTheme(mockJwt, request);

        // Then
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).subscribeToTheme(userUuid, themeUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

    @Test
    public void unsubscribeFromTheme_ShouldUnsubscribeUserFromTheme() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        String themeUuid = UUID.randomUUID().toString();
        SubscriptionRequest request = new SubscriptionRequest(themeUuid);

        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.doNothing().when(mockSubscriptionService).unsubscribeFromTheme(userUuid, themeUuid);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.unsubscribeFromTheme(mockJwt, request);

        // Then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).unsubscribeFromTheme(userUuid, themeUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

    @Test
    public void unsubscribeFromTheme_ShouldReturnNotFound_WhenNotFoundExceptionThrown() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        String themeUuid = UUID.randomUUID().toString();
        SubscriptionRequest request = new SubscriptionRequest(themeUuid);

        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.doThrow(new NotFoundException()).when(mockSubscriptionService)
                .unsubscribeFromTheme(userUuid, themeUuid);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.unsubscribeFromTheme(mockJwt, request);

        // Then
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).unsubscribeFromTheme(userUuid, themeUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

    @Test
    public void unsubscribeFromTheme_ShouldReturnBadRequest_WhenIllegalArgumentExceptionThrown() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        String themeUuid = "invalid-uuid";
        SubscriptionRequest request = new SubscriptionRequest(themeUuid);

        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.doThrow(new IllegalArgumentException("Invalid UUID")).when(mockSubscriptionService)
                .unsubscribeFromTheme(userUuid, themeUuid);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.unsubscribeFromTheme(mockJwt, request);

        // Then
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).unsubscribeFromTheme(userUuid, themeUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

    @Test
    public void unsubscribeFromTheme_ShouldReturnInternalServerError_WhenOtherExceptionThrown() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        String themeUuid = UUID.randomUUID().toString();
        SubscriptionRequest request = new SubscriptionRequest(themeUuid);

        Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
        Mockito.doThrow(new RuntimeException("Database error")).when(mockSubscriptionService)
                .unsubscribeFromTheme(userUuid, themeUuid);

        // When
        ResponseEntity<ApiResponse> response = classUnderTest.unsubscribeFromTheme(mockJwt, request);

        // Then
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Mockito.verify(mockSubscriptionService).unsubscribeFromTheme(userUuid, themeUuid);
        Mockito.verifyNoInteractions(mockThemeMapper);
    }

}
