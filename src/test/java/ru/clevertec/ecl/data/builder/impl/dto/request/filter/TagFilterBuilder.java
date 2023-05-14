package ru.clevertec.ecl.data.builder.impl.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.request.filter.impl.TagFilter;
import ru.clevertec.ecl.data.builder.EntityBuilder;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aTagFilter")
public class TagFilterBuilder implements EntityBuilder<TagFilter> {

    private Long id = 1L;
    private String name = "n1";

    @Override
    public TagFilter build() {
        return TagFilter.builder()
                .id(id)
                .name(name)
                .build();
    }

}