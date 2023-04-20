package ru.clevertec.ecl.dto.request;

import lombok.Builder;

@Builder
public record TagRequest(
        Long id,
        String name
) {

}