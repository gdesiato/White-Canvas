package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.dto.WhiteCanvasToken;
import com.desiato.whitecanvas.model.Session;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.repository.SessionRepository;
import com.desiato.whitecanvas.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public Session createSession(User user) {
        String tokenValue = UUID.randomUUID().toString();
        Session newSession = new Session(tokenValue, user.getId());
        sessionRepository.save(newSession);
        return newSession;
    }

    public Optional<User> findUserByToken(WhiteCanvasToken whiteCanvasToken) {
        return sessionRepository.findById(whiteCanvasToken.toString())
                .map(Session::getUserId)
                .flatMap(userRepository::findById);
    }

    public void deleteUserSessions(Long userId) {
        sessionRepository.deleteByUserId(userId);
    }
}
