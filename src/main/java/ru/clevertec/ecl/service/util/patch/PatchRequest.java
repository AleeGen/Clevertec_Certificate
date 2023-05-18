package ru.clevertec.ecl.service.util.patch;

import jakarta.validation.constraints.NotBlank;

/**
 * An entity request for working with {@link Patch}
 *
 * @param field field which will be replaced
 * @param value the value in json format that should be set
 */
public record PatchRequest(@NotBlank
                           String field,

                           @NotBlank
                           String value) {
}