package ru.clevertec.ecl.util.builder.impl.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.util.builder.EntityBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aGCRes")
public class GCResBuilder implements EntityBuilder<GiftCertificateResponse> {

    private Long id = 1L;
    private String name = "n1";
    private String description = "d1";
    private Double price = 1.0;
    private Integer duration = 1;
    private LocalDateTime createDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
    private LocalDateTime lastUpdateDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
    private List<TagResponse> tags = new ArrayList<>(List.of(
            TagResBuilder.aTagRes().withId(1L).withName("t1").build(),
            TagResBuilder.aTagRes().withId(2L).withName("t2").build(),
            TagResBuilder.aTagRes().withId(3L).withName("t3").build()));


    @Override
    public GiftCertificateResponse build() {
        return GiftCertificateResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .tags(tags)
                .build();
    }

}