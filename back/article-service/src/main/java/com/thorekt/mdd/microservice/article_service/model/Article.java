package com.thorekt.mdd.microservice.article_service.model;

import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Article entity
 * 
 * @author thorekt
 */
@Entity
@Table(name = "articles")
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = { "uuid" })
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Article {
    @Id
    @GeneratedValue
    private UUID uuid;

    @NonNull
    private String title;

    @NonNull
    private String content;

    @NonNull
    private UUID userUuid;

    @NonNull
    private UUID themeUuid;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private String createdAt;
}
