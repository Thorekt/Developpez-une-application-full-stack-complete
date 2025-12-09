package com.thorekt.mdd.microservice.article_service.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.thorekt.mdd.microservice.article_service.dto.ArticleDto;
import com.thorekt.mdd.microservice.article_service.model.Article;

@Component
@Mapper(componentModel = "spring")
public interface ArticleMapper extends EntityMapper<ArticleDto, Article> {

}
