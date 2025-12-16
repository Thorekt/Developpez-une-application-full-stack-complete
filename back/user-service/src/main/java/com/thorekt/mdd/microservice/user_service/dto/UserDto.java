package com.thorekt.mdd.microservice.user_service.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thorekt.mdd.microservice.user_service.dto.response.ApiResponse;

import lombok.*;

/**
 * Data Transfer Object for User
 * 
 * @author thorekt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements ApiResponse {
    private UUID uuid;

    @NonNull
    private String email;

    @NonNull
    private String username;

    @JsonIgnore
    private String password;
}
