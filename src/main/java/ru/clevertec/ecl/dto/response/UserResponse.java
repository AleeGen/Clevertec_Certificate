package ru.clevertec.ecl.dto.response;

import lombok.Builder;
import ru.clevertec.ecl.entity.Order;

import java.util.List;

@Builder
public record UserResponse(Long id,
                           String firstname,
                           String lastname,
                           List<Order> orders) {
}