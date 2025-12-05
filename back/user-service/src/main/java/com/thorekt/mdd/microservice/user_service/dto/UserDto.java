package com.thorekt.mdd.microservice.user_service.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thorekt.mdd.microservice.user_service.dto.response.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements ApiResponse {
    private UUID id;

    @NonNull
    private String email;

    @NonNull
    private String username;

    @JsonIgnore
    private String password;
}
