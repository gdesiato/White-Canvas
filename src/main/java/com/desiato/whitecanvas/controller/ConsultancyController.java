package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.ConsultancyProductDTO;
import com.desiato.whitecanvas.model.ConsultancyProduct;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/consultancy")
public class ConsultancyController {

    @GetMapping
    public ResponseEntity<List<ConsultancyProductDTO>> getAllConsultancyServices() {
        List<ConsultancyProductDTO> productsDTO = Arrays.stream(ConsultancyProduct.values())
                .map(consultancyProduct ->
                        new ConsultancyProductDTO(
                                consultancyProduct.getServiceName(),
                                consultancyProduct.getPrice()))
                .toList();
        return new ResponseEntity<>(productsDTO, HttpStatus.OK);
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
