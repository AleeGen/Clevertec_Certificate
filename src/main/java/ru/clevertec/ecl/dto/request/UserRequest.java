package ru.clevertec.ecl.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRequest(Long id,

                          @Size(max = 30)
                          @NotNull
                          String firstname,

                          @Size(max = 30)
                          @NotNull
                          String lastname) {
}