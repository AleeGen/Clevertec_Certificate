package ru.clevertec.ecl.util.builder.impl.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.util.builder.EntityBuilder;
import ru.clevertec.ecl.dto.request.TagRequest;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aTagReq")
public class TagReqBuilder implements EntityBuilder<TagRequest> {

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