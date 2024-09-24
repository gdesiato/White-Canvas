package com.desiato.whitecanvas.dto;

import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.model.OrderItem;
import com.desiato.whitecanvas.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoMapper {
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
                orderItem.getService().getServiceName(),
                orderItem.getCartItem().getQuantity(),
                orderItem.getService().getPrice()
        );
    }

    public CartResponseDTO toCartResponseDTO(Cart cart, Long userId) {
        return new CartResponseDTO(
                cart.getId(),
                userId,
                cart.getTotalPrice()
        );
    }

    public UserResponseDto toUserResponseDTO(User user) {
        List<OrderResponseDTO> orderResponseDTOList = user.getOrders().stream()
                .map(this::toOrderResponseDTO)
                .toList();

        CartResponseDTO cartResponseDTO = toCartResponseDTO(user.getCart(), user.getId());

        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                orderResponseDTOList,
                cartResponseDTO
        );
    }
}
