package ru.clevertec.ecl.dto.request.filter.impl;

import jakarta.persistence.Column;
import lombok.Builder;
import ru.clevertec.ecl.dto.request.filter.EntityFilter;

import java.time.LocalDateTime;

@Builder
public record GiftCertificateFilter(Long id,
                                    String name,
                                    String description,
                                    Double price,
                                    Integer duration,
                                    LocalDateTime createDate,
                                    LocalDateTime lastUpdateDate,
                                    String tagName,
                                    String part) implements EntityFilter {
}