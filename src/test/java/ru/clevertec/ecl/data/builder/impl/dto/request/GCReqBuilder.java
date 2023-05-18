package ru.clevertec.ecl.data.builder.impl.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.data.builder.EntityBuilder;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;

import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aGCReq")
public class GCReqBuilder implements EntityBuilder<GiftCertificateRequest> {

    private Long id = 1L;
    private String name = "n1";
    private String description = "d1";
    private Double price = 1.0;
    private Integer duration = 1;
    private List<TagRequest> tags = new ArrayList<>(List.of(
            TagReqBuilder.aTagReq().withId(1L).withName("t1").build(),
            TagReqBuilder.aTagReq().withId(2L).withName("t2").build(),
            TagReqBuilder.aTagReq().withId(3L).withName("t3").build()));

    @Override
    public GiftCertificateRequest build() {
        return GiftCertificateRequest.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .tags(tags)
                .build();
    }

}