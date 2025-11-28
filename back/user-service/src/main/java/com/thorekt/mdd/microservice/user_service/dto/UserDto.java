package com.thorekt.mdd.microservice.user_service.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
    @Size(max = 50)
    @Email
    private String email;

    @NonNull
    @Size(min = 3, max = 50)
    private String username;
}
