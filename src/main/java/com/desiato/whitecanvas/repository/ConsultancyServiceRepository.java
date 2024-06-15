package com.desiato.whitecanvas.repository;

import com.desiato.whitecanvas.model.ConsultancyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultancyServiceRepository extends JpaRepository<ConsultancyService, Long> {
    ConsultancyService findByConsultancyName(String consultancyName);
}
