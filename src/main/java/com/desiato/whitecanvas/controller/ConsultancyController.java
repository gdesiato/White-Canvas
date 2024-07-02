package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.model.ConsultancyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/consultancy")
public class ConsultancyController {

    @GetMapping("/all")
    public ResponseEntity<ConsultancyService[]> getAllConsultancyServices() {
        return ResponseEntity.ok(ConsultancyService.values());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ConsultancyService> getConsultancyService(@PathVariable String name) {
        try {
            ConsultancyService consultancyService = ConsultancyService.valueOf(name.toUpperCase());
            return ResponseEntity.ok(consultancyService);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
