package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.dto.DtoMapper;
import com.desiato.whitecanvas.dto.OrderResponseDTO;
import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final DtoMapper dtoMapper;

    @GetMapping("/order-confirmation")
    public ResponseEntity<OrderResponseDTO> showOrderConfirmation(@RequestParam("orderId") Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        OrderResponseDTO orderDTO = dtoMapper.toOrderResponseDTO(order);
        return ResponseEntity.ok(orderDTO);
    }
}
