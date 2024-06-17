package com.desiato.whitecanvas.repository;

import com.desiato.whitecanvas.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    void deleteByUserId(Long userId);
    Optional<Session> findByToken(String token);
}
