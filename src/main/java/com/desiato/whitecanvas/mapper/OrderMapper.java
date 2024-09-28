package com.desiato.whitecanvas.mapper;

import com.desiato.whitecanvas.dto.OrderItemDTO;
import com.desiato.whitecanvas.dto.OrderResponseDTO;
import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponseDTO toOrderResponseDTO(Order order) {
        List<OrderItemDTO> orderItemDTOs = order.getItems().stream()
                .map(this::toOrderItemDTO)
                .toList();
        return new OrderResponseDTO(
                order.getId(),
                order.getUser().getId(),
                orderItemDTOs,
                order.getOrderDate(),
                order.getTotalAmount()
        );
    }

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getService(),
                orderItem.getCartItem().getQuantity(),
                orderItem.getOrder()
        );
    }

    public OrderItem toOrderItem(OrderItemDTO dto) {
        OrderItem item = new OrderItem();
        item.setId(dto.id());
        item.setService(dto.service());
        item.setOrder(dto.order());
        return item;
    }
}
