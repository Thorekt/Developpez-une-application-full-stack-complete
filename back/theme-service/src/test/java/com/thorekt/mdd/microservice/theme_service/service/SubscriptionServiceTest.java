package com.thorekt.mdd.microservice.theme_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thorekt.mdd.microservice.theme_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.theme_service.model.Subscription;
import com.thorekt.mdd.microservice.theme_service.model.Theme;
import com.thorekt.mdd.microservice.theme_service.repository.SubscriptionRepository;
import com.thorekt.mdd.microservice.theme_service.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
    @Mock
    SubscriptionRepository mockSubscriptionRepository;

    @Mock
    ThemeRepository mockThemeRepository;

    SubscriptionService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new SubscriptionService(mockSubscriptionRepository, mockThemeRepository);
    }

    @Test
    public void subscribeToTheme_ShouldInvokeRepositoryMethod() {
        // Given
        UUID userUuid = UUID.randomUUID();
        UUID themeUuid = UUID.randomUUID();
        Theme theme = Theme.builder()
                .uuid(themeUuid)
                .title("Test Theme")
                .description("This is a test theme")
                .build();

        Mockito.when(mockThemeRepository.findByUuid(themeUuid)).thenReturn(theme);

        // When
        classUnderTest.subscribeToTheme(userUuid.toString(), themeUuid.toString());

        // Then
        Mockito.verify(mockSubscriptionRepository).save(Mockito.any());
        Mockito.verify(mockThemeRepository).findByUuid(themeUuid);
    }

    @Test
    public void subscribeToTheme_ShouldThrowNotFoundException_WhenThemeNotFound() {
        // Given
        UUID userUuid = UUID.randomUUID();
        UUID themeUuid = UUID.randomUUID();
        Mockito.when(mockThemeRepository.findByUuid(themeUuid)).thenReturn(null);

        // When / Then
        try {
            classUnderTest.subscribeToTheme(userUuid.toString(), themeUuid.toString());
            assertEquals(true, false);
        } catch (Exception e) {
            assertEquals(e.getClass(), NotFoundException.class);
        }

        // Then
        Mockito.verify(mockThemeRepository).findByUuid(themeUuid);
        Mockito.verifyNoMoreInteractions(mockSubscriptionRepository);
    }

    @Test
    public void unsubscribeFromTheme_ShouldInvokeRepositoryMethod() {
        // Given
        UUID userUuid = UUID.randomUUID();
        UUID themeUuid = UUID.randomUUID();
        Theme theme = Theme.builder()
                .uuid(themeUuid)
                .title("Test Theme")
                .description("This is a test theme")
                .build();
        Subscription subscription = Subscription.builder()
                .uuid(UUID.randomUUID())
                .userUuid(userUuid)
                .theme(theme)
                .build();

        // When
        Mockito.when(mockSubscriptionRepository.findByUserUuidAndTheme_Uuid(userUuid, themeUuid))
                .thenReturn(subscription);
        classUnderTest.unsubscribeFromTheme(userUuid.toString(), themeUuid.toString());

        // Then
        Mockito.verify(mockSubscriptionRepository).findByUserUuidAndTheme_Uuid(userUuid, themeUuid);
        Mockito.verify(mockSubscriptionRepository).deleteByUuid(subscription.getUuid());
    }

    @Test
    public void unsubscribeFromTheme_ShouldThrowNotFoundException_WhenSubscriptionNotFound() {
        // Given
        UUID userUuid = UUID.randomUUID();
        UUID themeUuid = UUID.randomUUID();
        Mockito.when(mockSubscriptionRepository.findByUserUuidAndTheme_Uuid(userUuid, themeUuid))
                .thenReturn(null);

        // When / Then
        try {
            classUnderTest.unsubscribeFromTheme(userUuid.toString(), themeUuid.toString());
            assertEquals(true, false);
        } catch (Exception e) {
            assertEquals(e.getClass(), NotFoundException.class);
        }

        // Then
        Mockito.verify(mockSubscriptionRepository).findByUserUuidAndTheme_Uuid(userUuid, themeUuid);
        Mockito.verifyNoMoreInteractions(mockSubscriptionRepository);
    }

    @Test
    public void findSubscriptionsByUserUuid_ShouldInvokeRepositoryMethod() {
        // Given
        UUID userUuid = UUID.randomUUID();
        Subscription subscription1 = Subscription.builder()
                .uuid(UUID.randomUUID())
                .userUuid(userUuid)
                .theme(Theme.builder()
                        .uuid(UUID.randomUUID())
                        .title("Test Theme 1")
                        .description("This is a test theme 1")
                        .build())
                .build();
        Subscription subscription2 = Subscription.builder()
                .uuid(UUID.randomUUID())
                .userUuid(userUuid)
                .theme(Theme.builder()
                        .uuid(UUID.randomUUID())
                        .title("Test Theme 2")
                        .description("This is a test theme 2")
                        .build())
                .build();
        List<Subscription> subscriptions = List.of(subscription1, subscription2);

        Mockito.when(mockSubscriptionRepository.findAllByUserUuid(userUuid))
                .thenReturn(subscriptions);

        // When
        List<Subscription> result = classUnderTest.findSubscriptionsByUserUuid(userUuid.toString());

        // Then
        Mockito.verify(mockSubscriptionRepository).findAllByUserUuid(userUuid);
        assertEquals(subscriptions, result);
    }
}