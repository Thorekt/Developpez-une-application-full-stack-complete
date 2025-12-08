package com.thorekt.mdd.microservice.theme_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.thorekt.mdd.microservice.theme_service.model.Subscription;

/**
 * Repository for Subscription entity
 * 
 * @author thorekt
 */
@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, UUID> {

    /**
     * Find a subscription by UUID
     * 
     * @param uuid UUID of the subscription
     * @return Subscription entity
     */
    Subscription findByUuid(UUID uuid);

    /**
     * Find all subscriptions by user UUID
     * 
     * @param userUuid UUID of the user
     * @return List of Subscription entities
     */
    List<Subscription> findAllByUserUuid(UUID userUuid);

    /**
     * Find a subscription by user UUID and theme UUID
     * 
     * @param userUuid  UUID of the user
     * @param themeUuid UUID of the theme
     * @return Subscription entity
     */
    Subscription findByUserUuidAndTheme_Uuid(UUID userUuid, UUID themeUuid);

    /**
     * Find all subscriptions by theme UUID
     * 
     * @param themeUuid UUID of the theme
     * @return List of Subscription entities
     */
    List<Subscription> findAllByTheme_Uuid(UUID themeUuid);

    /**
     * Delete a subscription by UUID
     * 
     * @param uuid UUID of the subscription
     */
    void deleteByUuid(UUID uuid);
}
