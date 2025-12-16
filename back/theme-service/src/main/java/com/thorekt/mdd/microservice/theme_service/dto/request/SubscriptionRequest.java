package com.thorekt.mdd.microservice.theme_service.dto.request;

/**
 * Request DTO for subscribing to a theme
 * 
 * @param themeUuid UUID of the theme to subscribe to
 * 
 * @author thorekt
 */
public record SubscriptionRequest(String themeUuid) {

}
