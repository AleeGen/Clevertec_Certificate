package ru.clevertec.ecl.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

@Builder
public record GiftCertificateRequest(Long id,
                                     @Size(max = 30)
                                     @NotBlank
                                     String name,

                                     @Size(max = 100)
                                     @NotBlank
                                     String description,

                                     @Positive
                                     Double price,

                                     @Positive
                                     Integer duration,

                                     List<TagRequest> tags) {
}