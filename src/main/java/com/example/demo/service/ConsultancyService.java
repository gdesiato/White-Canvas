package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Services;
import com.example.demo.repository.ConsultancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultancyService {

    @Autowired
    private ConsultancyRepository servicesRepository;


    public Services getServiceById(Long serviceId) {
        return servicesRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + serviceId));
    }

    public Services save(Services service) {
        return servicesRepository.save(service);
    }

}
