package ru.clevertec.ecl.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TagRequest(Long id,

                         @Size(max = 30)
                         String name) {
}