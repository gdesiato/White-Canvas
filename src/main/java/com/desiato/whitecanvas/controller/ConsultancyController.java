package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.ConsultancyProductDTO;
import com.desiato.whitecanvas.model.ConsultancyProduct;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all consultancy services",
            description = "Retrieves a list of all available consultancy services")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of consultancy services",
                    content = @Content(schema = @Schema(implementation = ConsultancyProductDTO.class),
                            examples = @ExampleObject(value = "[{ " +
                                    "\"serviceName\": \"Consulting Service A\", " +
                                    "\"price\": 100.0 }, " +
                                    "{ \"serviceName\": \"Consulting Service B\", " +
                                    "\"price\": 200.0 }]")))
    })
    public ResponseEntity<List<ConsultancyProductDTO>> getAllConsultancyServices() {
        List<ConsultancyProductDTO> productsDTO = Arrays.stream(ConsultancyProduct.values())
                .map(service -> new ConsultancyProductDTO(service.getServiceName(), service.getPrice()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productsDTO);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get consultancy service by name",
            description = "Retrieves the consultancy service details for the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved consultancy service",
                    content = @Content(schema = @Schema(implementation = ConsultancyProduct.class),
                            examples = @ExampleObject(value = "{ \"serviceName\": \"Consulting Service A\", " +
                                    "\"price\": 100.0 }"))),
            @ApiResponse(responseCode = "404", description = "Consultancy service not found")
    })
    public ResponseEntity<ConsultancyProduct> getConsultancyService(@PathVariable String name) {
        try {
            ConsultancyProduct consultancyProduct = ConsultancyProduct.valueOf(name.toUpperCase());
            return ResponseEntity.ok(consultancyProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
