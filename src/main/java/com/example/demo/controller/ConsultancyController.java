package com.example.demo.controller;

import com.example.demo.model.Consultancy;
import com.example.demo.repository.ConsultancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/consultancy")
public class ConsultancyController {

    private ConsultancyRepository consultancyRepository;

    public ConsultancyController(ConsultancyRepository consultancyRepository) {
        this.consultancyRepository = consultancyRepository;
    }

    @GetMapping
    public ResponseEntity<List<Consultancy>> getAllConsultancies() {
        List<Consultancy> listOfConsultancies = consultancyRepository.findAll();
        return ResponseEntity.ok(listOfConsultancies);
    }

    @GetMapping("/new")
    public ResponseEntity<Consultancy> showAddConsultancyForm() {
        Consultancy consultancy = new Consultancy();
        return ResponseEntity.ok(consultancy);
    }

    @PostMapping
    public ResponseEntity<Consultancy> addService(@RequestBody Consultancy consultancy) {
        Consultancy savedConsultancy = consultancyRepository.save(consultancy);
        return ResponseEntity.ok(savedConsultancy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultancy> getConsultancy(@PathVariable Long id) {
        Optional<Consultancy> consultancy = consultancyRepository.findById(id);
        return consultancy.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/update")
    public ResponseEntity<Consultancy> showUpdateConsultancyForm(@PathVariable Long id) {
        Optional<Consultancy> service = consultancyRepository.findById(id);
        return service.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<Consultancy> updateConsultancy(@PathVariable Long id, @RequestBody Consultancy updateConsultancy) {
        return consultancyRepository.findById(id)
                .map(existingConsultancy -> {
                    existingConsultancy.setServiceName(updateConsultancy.getServiceName());
                    existingConsultancy.setDescription(updateConsultancy.getDescription());
                    existingConsultancy.setCost(updateConsultancy.getCost());
                    Consultancy savedService = consultancyRepository.save(existingConsultancy);
                    return ResponseEntity.ok(savedService);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteConsultancy(@PathVariable Long id) {
        try {
            consultancyRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
