package ru.clevertec.ecl.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.builder.EntityBuilder;
import ru.clevertec.ecl.dto.request.TagRequest;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aTagRequest")
public class TagRequestBuilder implements EntityBuilder<TagRequest> {

    private Long id = 0L;
    private String name = "n0";

    @Override
    public TagRequest build() {
        return TagRequest.builder()
                .id(id)
                .name(name)
                .build();
    }

}