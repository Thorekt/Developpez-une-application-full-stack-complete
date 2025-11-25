# Microservices Architecture – Spring Boot 4 / Spring Cloud 2025

Ce dossier contient la structure initiale d’une architecture microservices basée sur Spring Boot 4 et Spring Cloud 2025.

## Structure du projet

Le répertoire `back/` contient les services suivants :

- api-gateway/
- discovery-service/
- config-server/
- user-service/
- theme-service/
- article-service/
- config-repo/

Chaque dossier correspond à une application Spring Boot indépendante générée via Spring Initializr, excepté pour config-repo.

## Description des services

### 1. API Gateway

Service frontal chargé de router les requêtes vers les microservices internes.
Utilise Spring Cloud Gateway (Reactive) et Discovery Client pour la résolution dynamique des services.

### 2. Discovery Service

Service de découverte basé sur Eureka Server.
Les microservices s’enregistrent automatiquement afin de permettre un routage dynamique et indépendant des ports.

### 3. Config Server

Serveur de configuration centralisé basé sur Spring Cloud Config.
Les fichiers de configuration versionnés (YAML) seront stockés dans le répertoire `config-repo`.

### 4. User Service

Service dédié à la gestion des comptes utilisateurs.
Contient les fonctionnalités liées à l’inscription, la connexion, et le profil utilisateur.

### 5. Theme Service

Service dédié à la gestion des thèmes et abonnements des utilisateurs.

### 6. Article Service

Service dédié à la gestion des articles et des commentaires.

## Technologies principales

- Spring Boot 4.x
- Spring Cloud 2025.1.0 (Oakwood)
- Spring Web / WebFlux selon les services
- Spring Security
- Spring Data JPA
- Eureka Server / Eureka Client
- Spring Cloud Gateway
- Spring Cloud Config Server
- MySQL (une base par service)

## Objectifs de l’architecture

- Séparation claire des domaines fonctionnels
- Déploiement indépendant de chaque service
- Configuration centralisée
- Découverte dynamique des services via Eureka
