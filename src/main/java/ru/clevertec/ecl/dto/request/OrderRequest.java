package ru.clevertec.ecl.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderRequest(@NotNull
                           Long userId,

                           @NotNull
                           Long gcId) {
}