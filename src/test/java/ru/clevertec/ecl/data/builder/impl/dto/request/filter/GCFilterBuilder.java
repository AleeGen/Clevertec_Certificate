package ru.clevertec.ecl.data.builder.impl.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.request.filter.impl.GiftCertificateFilter;
import ru.clevertec.ecl.data.builder.EntityBuilder;

import java.time.LocalDateTime;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aGCFilter")
public class GCFilterBuilder implements EntityBuilder<GiftCertificateFilter> {

    private Long id = 1L;
    private String name = "n1";
    private String description = "d1";
    private Double price = 1.0;
    private Integer duration = 1;
    private LocalDateTime createDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
    private LocalDateTime lastUpdateDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
    private String tagName = "t1";
    private String part = "d";


    @Override
    public GiftCertificateFilter build() {
        return GiftCertificateFilter.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .tagName(tagName)
                .part(part)
                .build();
    }

}