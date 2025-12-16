package com.thorekt.mdd.microservice.theme_service.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.thorekt.mdd.microservice.theme_service.model.Theme;
import com.thorekt.mdd.microservice.theme_service.service.ThemeService;
import com.thorekt.mdd.microservice.theme_service.dto.ThemeDto;
import com.thorekt.mdd.microservice.theme_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.theme_service.dto.response.ErrorResponse;
import com.thorekt.mdd.microservice.theme_service.dto.response.ThemeListResponse;
import com.thorekt.mdd.microservice.theme_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.theme_service.mapper.ThemeMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for managing themes.
 * 
 * @author Thorekt
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/theme")
public class ThemeController {
    /**
     * Theme service.
     */
    private final ThemeService themeService;

    /**
     * Theme mapper.
     */
    private final ThemeMapper themeMapper;

    /**
     * Find all themes.
     * 
     * @return List of Theme entities
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAllThemes() {
        try {
            List<Theme> themes = themeService.findAllThemes();
            List<ThemeDto> themeDtos = themes.stream()
                    .map(themeMapper::toDto)
                    .toList();
            ThemeListResponse response = new ThemeListResponse(themeDtos);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

    /**
     * Find a theme by UUID.
     * 
     * @param uuid UUID of the theme
     * @return Theme entity
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse> findThemeByUuid(@PathVariable("uuid") String uuid) {
        try {
            Theme theme = themeService.findByUuid(uuid);
            ThemeDto themeDto = themeMapper.toDto(theme);
            return ResponseEntity.ok(themeDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

}
