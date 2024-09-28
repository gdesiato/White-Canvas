package com.desiato.whitecanvas.mapper;

import com.desiato.whitecanvas.dto.CartResponseDTO;
import com.desiato.whitecanvas.dto.OrderItemDTO;
import com.desiato.whitecanvas.dto.OrderResponseDTO;
import com.desiato.whitecanvas.dto.UserResponseDto;
import com.desiato.whitecanvas.model.Cart;
import com.desiato.whitecanvas.model.Order;
import com.desiato.whitecanvas.model.OrderItem;
import com.desiato.whitecanvas.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserMapper {

    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;


    public UserResponseDto toUserResponseDTO(User user) {
        List<OrderResponseDTO> orderResponseDTOList = user.getOrders().stream()
                .map(orderMapper::toOrderResponseDTO)
                .toList();

        CartResponseDTO cartResponseDTO = cartMapper.toCartResponseDTO(user.getCart(), user.getId());

        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                orderResponseDTOList,
                cartResponseDTO
        );
    }
}
