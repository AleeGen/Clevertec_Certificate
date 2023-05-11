package ru.clevertec.ecl.dto.request.filter.impl;

import lombok.Builder;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;

@Builder
public record GiftCertificateFilter(Long id,
                                    String name,
                                    String description,
                                    Double price,
                                    Integer duration,
                                    String tagName,
                                    String part) implements EntityFilter {
}