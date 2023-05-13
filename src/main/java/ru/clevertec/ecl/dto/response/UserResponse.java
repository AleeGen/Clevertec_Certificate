package ru.clevertec.ecl.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserResponse(Long id,
                           String firstname,
                           String lastname,
                           List<OrderResponse> orders) {
}