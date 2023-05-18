package ru.clevertec.ecl.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderResponse(Long id,
                            Double cost,
                            LocalDateTime date,
                            Long userId,
                            Long gcId) {
}