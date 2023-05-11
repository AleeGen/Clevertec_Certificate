package ru.clevertec.ecl.dto.request.filter.impl;

import lombok.Builder;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;

@Builder
public record OrderFilter(Long id,
                          Double cost,
                          Long userId,
                          Long gcId) implements EntityFilter {
}