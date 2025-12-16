package com.thorekt.mdd.microservice.theme_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.thorekt.mdd.microservice.theme_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.theme_service.model.Theme;
import com.thorekt.mdd.microservice.theme_service.model.Subscription;
import com.thorekt.mdd.microservice.theme_service.repository.SubscriptionRepository;
import com.thorekt.mdd.microservice.theme_service.repository.ThemeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service for subscription operations
 * 
 * @author Thorekt
 */
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final ThemeRepository themeRepository;

    /**
     * Create a subscription for a user to a theme
     * 
     * @param userUuidString  UUID of the user
     * @param themeUuidString UUID of the theme
     */
    public void subscribeToTheme(String userUuidString, String themeUuidString) {
        Theme theme = themeRepository.findByUuid(UUID.fromString(themeUuidString));
        UUID userUuid = UUID.fromString(userUuidString);
        if (theme == null) {
            throw new NotFoundException();
        }
        Subscription subscription = Subscription.builder()
                .userUuid(userUuid)
                .theme(theme)
                .build();
        subscriptionRepository.save(subscription);
    }

    /**
     * Remove a subscription for a user to a theme
     * 
     * @param userUuidString  UUID of the user
     * @param themeUuidString UUID of the theme
     */
    public void unsubscribeFromTheme(String userUuidString, String themeUuidString) {
        UUID userUuid = UUID.fromString(userUuidString);
        UUID themeUuid = UUID.fromString(themeUuidString);
        Subscription subscription = subscriptionRepository.findByUserUuidAndTheme_Uuid(userUuid, themeUuid);
        if (subscription == null) {
            throw new NotFoundException();
        }
        subscriptionRepository.deleteByUuid(subscription.getUuid());
    }

    /**
     * Find all subscriptions for a user
     * 
     * @param userUuidString UUID of the user
     * @return List of Subscription entities
     */
    public List<Subscription> findSubscriptionsByUserUuid(String userUuidString) {
        UUID userUuid = UUID.fromString(userUuidString);
        return subscriptionRepository.findAllByUserUuid(userUuid);
    }
}
