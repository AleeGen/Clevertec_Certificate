package ru.clevertec.ecl.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderRequest(@NotNull
                           @Positive
                           Long userId,

                           @NotNull
                           @Positive
                           Long gcId) {
}