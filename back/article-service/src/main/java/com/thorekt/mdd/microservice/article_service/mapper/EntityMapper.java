package com.thorekt.mdd.microservice.article_service.mapper;

import java.util.List;

/**
 * Generic interface for mapping between DTOs and Entities.
 * 
 * @param <D> DTO type
 * @param <E> Entity type
 * 
 * @author Thorekt
 */
public interface EntityMapper<D, E> {

    /**
     * Converts a DTO to an Entity.
     * 
     * @param dto the DTO to convert
     * @return the converted Entity
     */
    E toEntity(D dto);

    /**
     * Converts an Entity to a DTO.
     * 
     * @param entity the Entity to convert
     * @return the converted DTO
     */
    D toDto(E entity);

    /**
     * Converts a list of DTOs to a list of Entities.
     * 
     * @param dtoList the list of DTOs to convert
     * @return the list of converted Entities
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Converts a list of Entities to a list of DTOs.
     * 
     * @param entityList the list of Entities to convert
     * @return the list of converted DTOs
     */
    List<D> toDto(List<E> entityList);
}