package ru.clevertec.ecl.util.builder.impl.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.util.builder.EntityBuilder;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aTagRes")
public class TagResBuilder implements EntityBuilder<TagResponse> {

    private Long id = 0L;
    private String name = "n0";

    @Override
    public TagResponse build() {
        return TagResponse.builder()
                .id(id)
                .name(name)
                .build();
    }

}