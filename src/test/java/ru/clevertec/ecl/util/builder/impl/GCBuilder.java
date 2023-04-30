package ru.clevertec.ecl.util.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.util.builder.EntityBuilder;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aGC")
public class GCBuilder implements EntityBuilder<GiftCertificate> {

    private Long id = 0L;
    private String name = "n0";
    private String description = "d0";
    private Double price = 0.0;
    private Integer duration = 0;
    private LocalDateTime createDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
    private LocalDateTime lastUpdateDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
    private List<Tag> tags = new ArrayList<>(List.of(
            TagBuilder.aTag().withId(1L).withName("t1").build(),
            TagBuilder.aTag().withId(2L).withName("t2").build(),
            TagBuilder.aTag().withId(3L).withName("t3").build()));

    @Override
    public GiftCertificate build() {
        return GiftCertificate.builder()
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