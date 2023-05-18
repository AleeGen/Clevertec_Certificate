package ru.clevertec.ecl.data.builder.impl.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.data.builder.EntityBuilder;
import ru.clevertec.ecl.entity.Tag;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aTag")
public class TagBuilder implements EntityBuilder<Tag> {

    private Long id = 1L;
    private String name = "n1";

    @Override
    public Tag build() {
        return Tag.builder()
                .id(id)
                .name(name)
                .build();
    }

}