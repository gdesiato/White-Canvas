package com.example.demo.repository;

import com.example.demo.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConsultancyRepository extends JpaRepository<Services, Long> {
    Services findByConsultancyName(String serviceName);
}
