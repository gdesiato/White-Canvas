package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.dto.AuthenticationRequestDto;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public String authenticateAndGenerateToken(AuthenticationRequestDto request) {
        User user = findUserOrThrow(request.email());
        validatePassword(request.password(), user.getPassword());

        // Generate and return JWT
        return generateToken(user);
    }

    private String generateToken(User user) {
        Instant now = Instant.now();
        long expiry = 36000L; // 10 hours

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(user.getEmail())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            log.warn("Authentication failed: Incorrect password.");
            throw new AuthenticationException("Authentication failed: Incorrect password.") {};
        }
    }

    private User findUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Authentication failed: No user found for email: {}", email);
                    return new AuthenticationException("Authentication failed: No user found.") {};
                });
    }
}

