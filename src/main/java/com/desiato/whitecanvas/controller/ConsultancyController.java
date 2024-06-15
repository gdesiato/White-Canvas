package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.model.ConsultancyService;
import com.desiato.whitecanvas.repository.ConsultancyServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/consultancy")
public class ConsultancyController {

    private final ConsultancyServiceRepository consultancyServiceRepository;

    @GetMapping
    public ResponseEntity<List<ConsultancyService>> getAllConsultancies() {
        List<ConsultancyService> listOfConsultancies = consultancyServiceRepository.findAll();
        return ResponseEntity.ok(listOfConsultancies);
    }

    @PostMapping
    public ResponseEntity<ConsultancyService> addService(@RequestBody ConsultancyService consultancyService) {
        ConsultancyService savedConsultancyService = consultancyServiceRepository.save(consultancyService);
        return ResponseEntity.ok(savedConsultancyService);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultancyService> getConsultancy(@PathVariable Long id) {
        Optional<ConsultancyService> consultancy = consultancyServiceRepository.findById(id);
        return consultancy.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<ConsultancyService> updateConsultancy(@PathVariable Long id, @RequestBody ConsultancyService updateConsultancyService) {
        return consultancyServiceRepository.findById(id)
                .map(existingConsultancy -> {
                    existingConsultancy.setName(updateConsultancyService.getName());
                    existingConsultancy.setDescription(updateConsultancyService.getDescription());
                    existingConsultancy.setCost(updateConsultancyService.getCost());
                    ConsultancyService savedService = consultancyServiceRepository.save(existingConsultancy);
                    return ResponseEntity.ok(savedService);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteConsultancy(@PathVariable Long id) {
        try {
            consultancyServiceRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
