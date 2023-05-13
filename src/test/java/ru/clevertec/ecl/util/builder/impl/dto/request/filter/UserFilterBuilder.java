package ru.clevertec.ecl.util.builder.impl.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.request.filter.impl.UserFilter;
import ru.clevertec.ecl.util.builder.EntityBuilder;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserFilter")
public class UserFilterBuilder implements EntityBuilder<UserFilter> {

    private Long id = 1L;
    private String firstname = "first";
    private String lastname = "last";

    @Override
    public UserFilter build() {
        return UserFilter.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .build();
    }

}