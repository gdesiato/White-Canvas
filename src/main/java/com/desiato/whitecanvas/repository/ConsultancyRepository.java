package com.desiato.whitecanvas.repository;

import com.desiato.whitecanvas.model.Consultancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultancyRepository extends JpaRepository<Consultancy, Long> {
    Consultancy findByConsultancyName(String consultancyName);
}
