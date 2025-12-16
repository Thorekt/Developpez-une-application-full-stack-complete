package com.thorekt.mdd.microservice.theme_service.model;

import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Subscription entity
 * 
 * @author Thorekt
 */
@Entity
@Table(name = "subscriptions")
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = { "uuid" })
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Subscription {
    /**
     * UUID of the subscription.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    /**
     * Theme associated with the subscription.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "theme_uuid", nullable = false)
    private Theme theme;

    /**
     * User UUID associated with the subscription.
     */
    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;
}
