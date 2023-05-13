package ru.clevertec.ecl.dto.request.filter.impl;

import lombok.Builder;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;

import java.time.LocalDateTime;

@Builder
public record OrderFilter(Long id,
                          Double cost,
                          LocalDateTime date,
                          Long userId,
                          Long gcId) implements EntityFilter {
}