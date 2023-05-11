package ru.clevertec.ecl.service.util.patch;

import jakarta.validation.constraints.NotNull;

/**
 * An entity request for working with {@link Patch}
 *
 * @param field field which will be replaced
 * @param value the value in json format that should be set
 */
public record PatchRequest(@NotNull
                           String field,

                           @NotNull
                           String value) {
}