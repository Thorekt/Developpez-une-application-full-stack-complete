package com.thorekt.mdd.microservice.user_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.thorekt.mdd.microservice.user_service.dto.UserDto;
import com.thorekt.mdd.microservice.user_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.user_service.dto.response.ErrorResponse;
import com.thorekt.mdd.microservice.user_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.user_service.mapper.UserMapper;
import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
        @Mock
        private UserService mockUserService;
        @Mock
        private UserMapper mockUserMapper;

        private UserController classUnderTest;

        @BeforeEach
        public void setUp() {
                classUnderTest = new UserController(mockUserService, mockUserMapper);
        }

        @Test
        public void findById_shouldReturnValidUserResponse_whenUserExists() {
                // Given
                String uuidString = UUID.randomUUID().toString();
                UUID uuid = UUID.fromString(uuidString);
                String username = "test-user";
                String email = "test-user@example.com";
                String password = "hashed-password";
                User user = User.builder()
                                .uuid(uuid)
                                .username(username)
                                .email(email)
                                .password(password)
                                .build();
                UserDto userDto = UserDto.builder()
                                .uuid(uuid)
                                .username(username)
                                .email(email)
                                .build();

                Mockito.when(mockUserService.findByUuid(uuidString))
                                .thenReturn(user);

                Mockito.when(mockUserMapper.toDto(user))
                                .thenReturn(userDto);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findByUuid(uuidString);

                // Then
                assertEquals(uuid, ((UserDto) response.getBody()).getUuid());
                assertEquals(username, ((UserDto) response.getBody()).getUsername());
                assertEquals(email, ((UserDto) response.getBody()).getEmail());
                Mockito.verify(mockUserService).findByUuid(uuidString);
                Mockito.verify(mockUserMapper).toDto(user);
        }

        @Test
        public void findById_shouldReturnNotFoundResponse_whenUserDoesNotExist() {
                // Given
                String uuidString = UUID.randomUUID().toString();
                NotFoundException notFoundException = new NotFoundException();
                Mockito.when(mockUserService.findByUuid(uuidString))
                                .thenThrow(notFoundException);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findByUuid(uuidString);

                // Then
                assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
                assertEquals(notFoundException.getMessage(), ((ErrorResponse) response.getBody()).error());
                Mockito.verify(mockUserService).findByUuid(uuidString);
                Mockito.verifyNoInteractions(mockUserMapper);
        }

        @Test
        public void findById_shouldReturnBadRequestResponse_whenInvalidUuidFormatIsProvided() {
                // Given
                String invalidUuidString = "invalid-uuid-format";
                Mockito.when(mockUserService.findByUuid(invalidUuidString))
                                .thenThrow(new IllegalArgumentException());

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findByUuid(invalidUuidString);

                // Then
                assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
                assertEquals("INVALID_FORMAT", ((ErrorResponse) response.getBody()).error());
                Mockito.verify(mockUserService).findByUuid(invalidUuidString);
                Mockito.verifyNoInteractions(mockUserMapper);
        }

        @Test
        public void findById_shouldReturnInternalServerErrorResponse_whenUnexpectedExceptionIsThrown() {
                // Given
                String uuidString = UUID.randomUUID().toString();
                Mockito.when(mockUserService.findByUuid(uuidString))
                                .thenThrow(new RuntimeException());

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findByUuid(uuidString);

                // Then
                assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
                assertEquals("INTERNAL_SERVER_ERROR", ((ErrorResponse) response.getBody()).error());
                Mockito.verify(mockUserService).findByUuid(uuidString);
                Mockito.verifyNoInteractions(mockUserMapper);
        }
}