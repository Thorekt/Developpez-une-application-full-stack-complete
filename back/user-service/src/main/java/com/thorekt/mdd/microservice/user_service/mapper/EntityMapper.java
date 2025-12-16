package com.thorekt.mdd.microservice.user_service.mapper;

import java.util.List;

/**
 * Generic interface for mapping between DTOs and Entities
 * 
 * @param <D> DTO type
 * @param <E> Entity type
 * 
 * @author thorekt
 */
public interface EntityMapper<D, E> {

    /**
     * Convert DTO to Entity
     * 
     * @param dto DTO object
     * @return Entity object
     */
    E toEntity(D dto);

    /**
     * Convert Entity to DTO
     * 
     * @param entity Entity object
     * @return DTO object
     */
    D toDto(E entity);

    /**
     * Convert list of DTOs to list of Entities
     * 
     * @param dtoList List of DTO objects
     * @return List of Entity objects
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Convert list of Entities to list of DTOs
     * 
     * @param entityList List of Entity objects
     * @return List of DTO objects
     */
    List<D> toDto(List<E> entityList);
}