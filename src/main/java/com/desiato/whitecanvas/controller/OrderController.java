package com.desiato.whitecanvas.controller;

import com.desiato.whitecanvas.mapper.OrderMapper;
import com.desiato.whitecanvas.dto.OrderRequestDTO;
import com.desiato.whitecanvas.dto.OrderResponseDTO;
import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.service.OrderService;
import com.desiato.whitecanvas.validator.OrderRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderRequestValidator validator;
    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponseDTO> orderResponseDTOList = orders
                .stream()
                .map(orderMapper::toOrderResponseDTO)
                .toList();
        return new ResponseEntity<>(orderResponseDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        OrderResponseDTO orderDTO = orderMapper.toOrderResponseDTO(order);
        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long id,
            @RequestBody OrderRequestDTO orderRequestDTO) {

        validator.validate(orderRequestDTO);

        Order order = orderService.updateOrder(id, orderRequestDTO);
        return new ResponseEntity<>(orderMapper.toOrderResponseDTO(order), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
