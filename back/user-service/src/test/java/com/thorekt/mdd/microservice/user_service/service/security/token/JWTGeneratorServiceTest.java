package com.thorekt.mdd.microservice.user_service.service.security.token;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

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
public class JWTGeneratorServiceTest {

    @Mock
    JwtEncoder mockJwtEncoder;

    @Mock
    Authentication mockAuthentication;

    JWTGeneratorService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new JWTGeneratorService(mockJwtEncoder);

        ReflectionTestUtils.setField(classUnderTest, "issuer", "test-issuer");
        ReflectionTestUtils.setField(classUnderTest, "expiresIn", 3600L);
    }

    @Test
    public void generateToken_ShouldReturnValidJWT() {
        // Given
        UUID userUUID = UUID.randomUUID();
        String username = "test-user";

        Mockito.when(mockJwtEncoder.encode(Mockito.any(JwtEncoderParameters.class)))
                .thenReturn(new Jwt(
                        "dummy-token",
                        Instant.now(),
                        Instant.now().plusSeconds(3600),
                        Map.of("alg", "RS256"),
                        Map.of("sub", userUUID.toString(), "username", username)));
        Mockito.when(mockAuthentication.getName()).thenReturn(username);

        // When
        String jwt = classUnderTest.generateToken(mockAuthentication, userUUID);

        // Then
        assertNotNull(jwt);
        assertTrue(jwt.length() > 0);

        Mockito.verify(mockJwtEncoder).encode(Mockito.any(JwtEncoderParameters.class));
    }
}
