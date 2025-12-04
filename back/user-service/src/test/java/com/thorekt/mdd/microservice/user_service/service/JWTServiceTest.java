package com.thorekt.mdd.microservice.user_service.service;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    @Mock
    JwtEncoder mockJwtEncoder;

    @Mock
    Authentication mockAuthentication;

    JWTService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new JWTService(mockJwtEncoder);

        ReflectionTestUtils.setField(classUnderTest, "issuer", "test-issuer");
        ReflectionTestUtils.setField(classUnderTest, "expiresIn", 3600L);
    }

    @Test
    public void generateToken_ShouldReturnValidJWT() {
        // Given
        Mockito.when(mockAuthentication.getName()).thenReturn("test-user");
        Mockito.when(mockJwtEncoder.encode(Mockito.any(JwtEncoderParameters.class)))
                .thenReturn(new Jwt(
                        "dummy-token",
                        Instant.now(),
                        Instant.now().plusSeconds(3600),
                        Map.of("alg", "RS256"),
                        Map.of("sub", "test-user")));

        // When
        String jwt = classUnderTest.generateToken(mockAuthentication);

        // Then
        assertNotNull(jwt);
        assertTrue(jwt.length() > 0);

        Mockito.verify(mockAuthentication).getName();
        Mockito.verify(mockJwtEncoder).encode(Mockito.any(JwtEncoderParameters.class));
    }
}
