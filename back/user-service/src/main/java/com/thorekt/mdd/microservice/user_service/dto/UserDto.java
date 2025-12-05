package com.thorekt.mdd.microservice.user_service.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;

    @NonNull
    private String email;

    @NonNull
    private String username;

    @JsonIgnore
    private String password;
}
