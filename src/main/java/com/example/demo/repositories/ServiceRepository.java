package com.example.demo.repositories;

import com.example.demo.models.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {
    Services findByServiceName(String serviceName);
}