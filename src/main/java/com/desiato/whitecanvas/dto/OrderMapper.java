package com.desiato.whitecanvas.dto;

import com.desiato.whitecanvas.model.Order;
import org.springframework.stereotype.Component;

@Component
public interface OrderMapper {
    OrderDTO toDto(Order order);
}
