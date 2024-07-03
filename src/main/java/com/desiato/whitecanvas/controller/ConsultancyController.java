package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.ConsultancyProductDTO;
import com.desiato.whitecanvas.model.ConsultancyProduct;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/consultancy")
public class ConsultancyController {

    @GetMapping("/all")
    public ResponseEntity<List<ConsultancyProductDTO>> getAllConsultancyServices() {
        List<ConsultancyProductDTO> productsDTO = Arrays.stream(ConsultancyProduct.values())
                .map(service -> new ConsultancyProductDTO(service.getServiceName(), service.getPrice()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productsDTO);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ConsultancyProduct> getConsultancyService(@PathVariable String name) {
        try {
            ConsultancyProduct consultancyProduct = ConsultancyProduct.valueOf(name.toUpperCase());
            return ResponseEntity.ok(consultancyProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
