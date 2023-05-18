package ru.clevertec.ecl.dto.request.filter.impl;

import lombok.Builder;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;

@Builder
public record UserFilter(Long id,
                         String firstname,
                         String lastname) implements EntityFilter {
}