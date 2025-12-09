package com.thorekt.mdd.microservice.article_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.netflix.spectator.impl.PatternExpr.Or;
import com.thorekt.mdd.microservice.article_service.dto.ArticleDto;
import com.thorekt.mdd.microservice.article_service.dto.request.CreateArticleRequest;
import com.thorekt.mdd.microservice.article_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.article_service.dto.response.ArticleListResponse;
import com.thorekt.mdd.microservice.article_service.dto.response.ErrorResponse;
import com.thorekt.mdd.microservice.article_service.dto.response.SuccessResponse;
import com.thorekt.mdd.microservice.article_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.article_service.mapper.ArticleMapper;
import com.thorekt.mdd.microservice.article_service.model.Article;
import com.thorekt.mdd.microservice.article_service.service.ArticleService;

import lombok.RequiredArgsConstructor;
import utils.OrderEnum;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    private final ArticleMapper articleMapper;

    /**
     * Find all articles by themes UUIDs in order.
     * 
     * @param themeUuids List of UUIDs of the themes
     * @param order      OrderEnum indicating ascending or descending order
     * @return List of Article entities
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse> findArticlesByThemesUuidsInOrder(
            @RequestParam("theme_uuids") List<UUID> themeUuids,
            @RequestParam(value = "order", defaultValue = "DESC") OrderEnum order) {
        try {
            List<Article> articles = articleService.findAllArticlesByThemesUuidsInOrder(themeUuids, order);
            List<ArticleDto> articleDtos = articles.stream()
                    .map(articleMapper::toDto)
                    .toList();
            ArticleListResponse response = new ArticleListResponse(articleDtos);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

    /**
     * Find an article by UUID.
     * 
     * @param uuid UUID of the article
     * @return Article entity
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse> findArticleByUuid(@PathVariable("uuid") UUID uuid) {
        try {
            Article article = articleService.findArticleByUuid(uuid);
            ArticleDto articleDto = articleMapper.toDto(article);
            return ResponseEntity.ok(new ArticleListResponse(List.of(articleDto)));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

    /**
     * Create a new article.
     * 
     * @param userUuid             UUID of the user
     * @param createArticleRequest Request body containing themeUuid, title, and
     *                             content
     * @return ResponseEntity indicating the result of the operation
     */
    @PostMapping("/")
    public ResponseEntity<ApiResponse> createArticle(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CreateArticleRequest createArticleRequest) {
        try {
            String userUuid = jwt.getClaimAsString("sub");
            articleService.createArticle(
                    userUuid,
                    createArticleRequest.themeUuid(),
                    createArticleRequest.title(),
                    createArticleRequest.content());
            return ResponseEntity.ok().body(new SuccessResponse("ARTICLE_CREATED"));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }
}