package com.thorekt.mdd.microservice.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thorekt.mdd.microservice.user_service.dto.UserDto;
import com.thorekt.mdd.microservice.user_service.dto.request.UpdateRequest;
import com.thorekt.mdd.microservice.user_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.user_service.dto.response.ErrorResponse;
import com.thorekt.mdd.microservice.user_service.dto.response.SuccessResponse;
import com.thorekt.mdd.microservice.user_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.user_service.mapper.UserMapper;
import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.service.business.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller for user-related operations
 * 
 * @author Thorekt
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Find a user by UUID
     * 
     * @param uuid UUID of the user
     * @return UserDto of the found user
     * @throws NotFoundException
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse> findByUuid(@PathVariable("uuid") @Valid String uuid) {
        try {
            User user = userService.findByUuid(uuid);
            UserDto userDto = userMapper.toDto(user);
            return ResponseEntity.ok(userDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse> updateUser(@AuthenticationPrincipal Jwt jwt,
            @RequestBody UpdateRequest updateRequest) {
        String uuid = jwt.getClaimAsString("sub");
        try {
            userService.updateUser(uuid, updateRequest.email(), updateRequest.username(), updateRequest.password());

            return ResponseEntity.ok(new SuccessResponse("USER_UPDATED"));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }

    }

}
