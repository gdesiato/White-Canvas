package com.example.demo.repositories;

import com.example.demo.models.Services;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Services, Long> {
    Optional<Services> findById(Long id);
}