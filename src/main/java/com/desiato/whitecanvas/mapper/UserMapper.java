package com.desiato.whitecanvas.mapper;

import com.desiato.whitecanvas.dto.CartResponseDTO;
import com.desiato.whitecanvas.dto.OrderResponseDTO;
import com.desiato.whitecanvas.dto.UserResponseDTO;
import com.desiato.whitecanvas.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserMapper {

    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;


    public UserResponseDTO toUserResponseDTO(User user) {
        List<OrderResponseDTO> orderResponseDTOList = user.getOrders().stream()
                .map(orderMapper::toOrderResponseDTO)
                .toList();

        CartResponseDTO cartResponseDTO = cartMapper.toCartResponseDTO(user.getCart(), user.getId());

        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                orderResponseDTOList,
                cartResponseDTO
        );
    }
}
