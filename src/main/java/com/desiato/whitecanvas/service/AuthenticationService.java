package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.dto.AuthenticationRequestDto;
import com.desiato.whitecanvas.dto.UserToken;
import com.desiato.whitecanvas.dto.WhiteCanvasToken;
import com.desiato.whitecanvas.model.CustomUserDetails;
import com.desiato.whitecanvas.model.Session;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    public UserToken authenticate(AuthenticationRequestDto request) {

        User user = findUserOrThrow(request);

        validatePassword(request, user);

        Session session = sessionService.createSession(user);
        String tokenValue = session.getToken();

        return new UserToken(tokenValue);
    }

    public Optional<CustomUserDetails> createUserDetails(WhiteCanvasToken whiteCanvasToken) {
        return sessionService.findUserByToken(whiteCanvasToken)
                .map(CustomUserDetails::new);
    }

    private void validatePassword(AuthenticationRequestDto request, User user) {
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Authentication failed: Incorrect password for email: {}", request.email());
            throw new AuthenticationException("Authentication failed: Incorrect password.") {};
        }
    }

    private User findUserOrThrow(AuthenticationRequestDto request) {
        return userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    return new AuthenticationException("Authentication failed: No user found.") {};
                });
    }
}
