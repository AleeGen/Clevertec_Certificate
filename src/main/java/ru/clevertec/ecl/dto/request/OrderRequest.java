package ru.clevertec.ecl.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderRequest(@Positive
                           Long userId,

                           @Positive
                           Long gcId) {
}