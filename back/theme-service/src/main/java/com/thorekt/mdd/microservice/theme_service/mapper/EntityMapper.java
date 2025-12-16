package com.thorekt.mdd.microservice.theme_service.mapper;

import java.util.List;

/**
 * Generic interface for mapping between DTOs and Entities.
 * 
 * @param <D> DTO type
 * @param <E> Entity type
 */
public interface EntityMapper<D, E> {

    /**
     * Convert DTO to Entity.
     * 
     * @param dto Data Transfer Object
     * @return Entity
     */
    E toEntity(D dto);

    /**
     * Convert Entity to DTO.
     * 
     * @param entity Entity
     * @return Data Transfer Object
     */
    D toDto(E entity);

    /**
     * Convert list of DTOs to list of Entities.
     * 
     * @param dtoList List of Data Transfer Objects
     * @return List of Entities
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Convert list of Entities to list of DTOs.
     * 
     * @param entityList List of Entities
     * @return List of Data Transfer Objects
     */
    List<D> toDto(List<E> entityList);
}