package com.thorekt.mdd.microservice.theme_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thorekt.mdd.microservice.theme_service.dto.ThemeDto;
import com.thorekt.mdd.microservice.theme_service.dto.request.SubscriptionRequest;
import com.thorekt.mdd.microservice.theme_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.theme_service.dto.response.ErrorResponse;
import com.thorekt.mdd.microservice.theme_service.dto.response.ThemeListResponse;
import com.thorekt.mdd.microservice.theme_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.theme_service.mapper.ThemeMapper;
import com.thorekt.mdd.microservice.theme_service.model.Subscription;
import com.thorekt.mdd.microservice.theme_service.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller for managing subscriptions.
 * 
 * @author thorekt
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/theme/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    private final ThemeMapper themeMapper;

    /**
     * Find all theme susbscribed by the user.
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAllSubscribedThemes(@AuthenticationPrincipal Jwt jwt) {
        String uuid = jwt.getClaimAsString("sub");
        try {
            List<Subscription> subscriptions = subscriptionService.findSubscriptionsByUserUuid(uuid);
            List<ThemeDto> themeDtos = subscriptions.stream()
                    .map(Subscription::getTheme)
                    .map(themeMapper::toDto)
                    .toList();
            ThemeListResponse response = new ThemeListResponse(themeDtos);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

    /**
     * Subscribe the user to a theme.
     */
    @PostMapping("/")
    public ResponseEntity<ApiResponse> subscribeToTheme(@AuthenticationPrincipal Jwt jwt,
            @RequestBody SubscriptionRequest request) {
        String userUuid = jwt.getClaimAsString("sub");
        try {
            subscriptionService.subscribeToTheme(userUuid, request.theme_uuid());
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

    /**
     * Unsubscribe the user from a theme.
     */
    @DeleteMapping("/")
    public ResponseEntity<ApiResponse> unsubscribeFromTheme(@AuthenticationPrincipal Jwt jwt,
            @RequestBody SubscriptionRequest request) {
        String userUuid = jwt.getClaimAsString("sub");
        try {
            subscriptionService.unsubscribeFromTheme(userUuid, request.theme_uuid());
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }
}
