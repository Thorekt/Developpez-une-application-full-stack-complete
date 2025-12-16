package com.thorekt.mdd.microservice.theme_service.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.thorekt.mdd.microservice.theme_service.dto.ThemeDto;
import com.thorekt.mdd.microservice.theme_service.model.Theme;

/**
 * Mapper interface for converting between Theme entity and ThemeDto.
 * 
 * @author Thorekt
 */
@Component
@Mapper(componentModel = "spring")
public interface ThemeMapper extends EntityMapper<ThemeDto, Theme> {

}
