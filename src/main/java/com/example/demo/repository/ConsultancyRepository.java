package com.example.demo.repository;

import com.example.demo.model.Consultancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConsultancyRepository extends JpaRepository<Consultancy, Long> {
    Consultancy findByConsultancyName(String serviceName);
}
