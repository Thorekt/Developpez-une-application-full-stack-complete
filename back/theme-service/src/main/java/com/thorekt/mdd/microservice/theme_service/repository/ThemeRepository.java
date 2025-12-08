package com.thorekt.mdd.microservice.theme_service.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.thorekt.mdd.microservice.theme_service.model.Theme;
import java.util.List;

/**
 * Repository for Theme entity
 * 
 * @author thorekt
 */
@Repository
public interface ThemeRepository extends CrudRepository<Theme, UUID> {

    /**
     * Find all themes
     * 
     * @return List of Theme entities
     */
    List<Theme> findAll();

    /**
     * Find a theme by UUID
     * 
     * @param uuid UUID of the theme
     * @return Theme entity
     */
    Theme findByUuid(UUID uuid);
}
