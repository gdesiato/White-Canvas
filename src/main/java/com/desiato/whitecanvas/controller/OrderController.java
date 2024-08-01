package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.OrderDTO;
import com.desiato.whitecanvas.dto.OrderMapper;
import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/order-confirmation")
    @Operation(summary = "Show order confirmation", description = "Retrieves the order confirmation details for the specified order ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order confirmation",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class),
                            examples = @ExampleObject(value = "{ \"orderId\": 1, \"status\": \"CONFIRMED\", \"total\": 100.0 }"))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> showOrderConfirmation(@RequestParam("orderId") Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        OrderDTO orderDTO = orderMapper.toDto(order);
        return ResponseEntity.ok(orderDTO);
    }
}
