package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.exception.ResourceNotFoundException;
import com.desiato.whitecanvas.repository.ConsultancyServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ConsultancyServiceService {

    private ConsultancyServiceRepository consultancyServiceRepository;

    public com.desiato.whitecanvas.model.ConsultancyService getServiceById(Long serviceId) {
        return consultancyServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + serviceId));
    }

    public com.desiato.whitecanvas.model.ConsultancyService save(com.desiato.whitecanvas.model.ConsultancyService service) {
        return consultancyServiceRepository.save(service);
    }

}
