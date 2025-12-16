package com.thorekt.mdd.microservice.article_service.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.thorekt.mdd.microservice.article_service.dto.CommentDto;
import com.thorekt.mdd.microservice.article_service.model.Comment;

/**
 * Mapper interface for converting between Comment entity and CommentDto.
 * 
 * @author Thorekt
 */
@Component
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDto, Comment> {

}
