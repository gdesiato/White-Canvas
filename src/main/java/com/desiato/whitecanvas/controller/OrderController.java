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
    public ResponseEntity<OrderDTO> showOrderConfirmation(@RequestParam("orderId") Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        OrderDTO orderDTO = orderMapper.toDto(order);
        return ResponseEntity.ok(orderDTO);
    }
}
