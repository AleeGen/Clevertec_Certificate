package ru.clevertec.ecl.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record GiftCertificateRequest(Long id,
                                     @Size(max = 30)
                                     @NotNull
                                     String name,

                                     @Size(max = 100)
                                     @NotNull
                                     String description,

                                     @NotNull
                                     Double price,

                                     @NotNull
                                     Integer duration,

                                     @NotEmpty
                                     List<TagRequest> tags) {
}