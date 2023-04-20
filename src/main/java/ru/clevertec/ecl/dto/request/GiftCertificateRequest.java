package ru.clevertec.ecl.dto.request;

import lombok.Builder;

@Builder
public record GiftCertificateRequest(
        Long id,
        String name,
        String description,
        Double price,
        Integer duration
) {

}