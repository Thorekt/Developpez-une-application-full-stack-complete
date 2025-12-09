package com.thorekt.mdd.microservice.article_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.thorekt.mdd.microservice.article_service.model.Comment;

/**
 * Repository for Comment entity
 * 
 * @author thorekt
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, UUID> {

    /**
     * Find a comment by UUID
     * 
     * @param uuid UUID of the comment
     * @return Comment entity
     */
    Comment findByUuid(UUID uuid);

    /**
     * Find all comments for a given article (ordered latest first)
     * 
     * @param articleUuid UUID of the article
     * @return List of Comment entities
     */
    List<Comment> findAllByArticle_UuidOrderByCreatedAtDesc(UUID articleUuid);

    /**
     * Find a comment by UUID of the user
     * 
     * @param userUuid UUID of the user
     * @return Comment entity
     */
    List<Comment> findAllByUserUuid(UUID userUuid);

    /**
     * Delete a comment by UUID
     * 
     * @param uuid UUID of the comment
     */
    void deleteByUuid(UUID uuid);

}
