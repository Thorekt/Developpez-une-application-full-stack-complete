package com.thorekt.mdd.microservice.theme_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.thorekt.mdd.microservice.theme_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.theme_service.model.Theme;
import com.thorekt.mdd.microservice.theme_service.repository.ThemeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service for theme operations
 * 
 * @author Thorekt
 */
@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;

    /**
     * Find all themes
     * 
     * @return
     */
    public List<Theme> findAllThemes() {
        return themeRepository.findAll();
    }

    /**
     * Find a theme by UUID
     * 
     * @param uuid UUID of the theme
     * @return Theme entity
     */
    public Theme findByUuid(String uuid) throws NotFoundException {
        UUID themeUuid = UUID.fromString(uuid);
        Theme theme = themeRepository.findByUuid(themeUuid);
        if (theme == null) {
            throw new NotFoundException();
        }
        return theme;
    }
}