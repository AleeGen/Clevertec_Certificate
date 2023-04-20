package ru.clevertec.ecl.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GiftCertificateResponse(
        Long id,
        String name,
        String description,
        Double price,
        Integer duration,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
        LocalDateTime createDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
        LocalDateTime lastUpdateDate,
        List<TagResponse> tags
) {

}