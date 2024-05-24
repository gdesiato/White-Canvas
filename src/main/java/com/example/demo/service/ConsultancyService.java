package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Consultancy;
import com.example.demo.repository.ConsultancyRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsultancyService {

    private ConsultancyRepository consultancyRepository;

    public ConsultancyService(ConsultancyRepository consultancyRepository) {
        this.consultancyRepository = consultancyRepository;
    }

    public Consultancy getServiceById(Long serviceId) {
        return consultancyRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + serviceId));
    }

    public Consultancy save(Consultancy service) {
        return consultancyRepository.save(service);
    }

}
