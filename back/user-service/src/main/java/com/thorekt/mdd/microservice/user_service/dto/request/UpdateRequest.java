package com.thorekt.mdd.microservice.user_service.dto.request;

public record UpdateRequest(
        String email,
        String username,
        String password) {

}
