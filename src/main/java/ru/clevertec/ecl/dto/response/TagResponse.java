package ru.clevertec.ecl.dto.response;

import lombok.Builder;

@Builder
public record TagResponse(Long id,
                          String name) {
}