package ru.clevertec.ecl.dto.request.filter.impl;

import lombok.Builder;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;

@Builder
public record TagFilter(Long id,
                        String name) implements EntityFilter {
}