package com.desiato.whitecanvas.dto;

import com.desiato.whitecanvas.model.CartItem;
import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.model.OrderItem;
import com.desiato.whitecanvas.model.User;
import com.desiato.whitecanvas.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderMapperImpl implements OrderMapper {

    private final UserService userService;

    public OrderMapperImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OrderDTO toDto(Order order) {
        if (order == null) {
            return null;
        }

        UserDTO userDTO = mapUserToUserDTO(order.getUser());
        List<OrderItemDTO> items = order.getItems().stream()
                .map(this::mapOrderItemToOrderItemDTO)
                .collect(Collectors.toList());

        OrderDTO orderDTO = new OrderDTO(
                order.getId(),
                userDTO,
                items,
                order.getOrderDate(),
                order.getTotalAmount().setScale(2, RoundingMode.HALF_UP)
        );
        log.debug("Mapped Order to OrderDTO: {}", orderDTO);
        return orderDTO;
    }

    private UserDTO mapUserToUserDTO(User user) {
        return userService.getUserById(user.getId())
                .map(u -> new UserDTO(u.getId(), u.getEmail()))
                .orElse(null);
    }

    private OrderItemDTO mapOrderItemToOrderItemDTO(OrderItem item) {
        CartItem cartItem = item.getCartItem();
        BigDecimal price = item.getService().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return new OrderItemDTO(
                item.getId(),
                item.getService().name(),
                cartItem.getQuantity(),
                price.setScale(2, RoundingMode.HALF_UP));
    }
}
