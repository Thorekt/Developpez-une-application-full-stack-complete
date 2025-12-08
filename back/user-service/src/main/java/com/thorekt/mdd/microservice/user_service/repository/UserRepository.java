package com.thorekt.mdd.microservice.user_service.repository;

import java.util.UUID;

import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties.Apiversion.Use;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.thorekt.mdd.microservice.user_service.model.User;

/**
 * Repository for User entity
 * 
 * @author thorekt
 */
@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    /**
     * Find a user by email
     * 
     * @param email Email of the user
     * @return User entity
     */
    User findByEmail(String email);

    /**
     * Find a user by username
     * 
     * @param username Username of the user
     * @return User entity
     */
    User findByUsername(String username);

    /**
     * Find a user by email or username
     * 
     * @param email    Email of the user
     * @param username Username of the user
     * @return User entity
     */
    User findByEmailOrUsername(String email, String username);

    /**
     * Find a user by UUID
     * 
     * @param uuid UUID of the user
     * @return User entity
     */
    User findByUuid(UUID uuid);

    /**
     * Check if a user exists by email
     * 
     * @param email
     * @return
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user exists by username
     * 
     * @param username
     * @return
     */
    boolean existsByUsername(String username);
}
