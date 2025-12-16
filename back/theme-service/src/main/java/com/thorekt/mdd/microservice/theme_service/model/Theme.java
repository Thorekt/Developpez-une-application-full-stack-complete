package com.thorekt.mdd.microservice.theme_service.model;

import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
 * Theme entity
 * 
 * @author Thorekt
 */
@Entity
@Table(name = "themes", uniqueConstraints = {
        @UniqueConstraint(columnNames = "title")
})
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = { "uuid" })
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Theme {
    /**
     * UUID of the theme.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    /**
     * Title of the theme.
     */
    @NonNull
    private String title;

    /**
     * Description of the theme.
     */
    @Column(columnDefinition = "TEXT")
    @NonNull
    private String description;

}
