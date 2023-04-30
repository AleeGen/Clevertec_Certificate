package ru.clevertec.ecl.util.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.util.builder.EntityBuilder;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aGCRequest")
public class GCRequestBuilder implements EntityBuilder<GiftCertificateRequest> {

    private Long id = 0L;
    private String name = "n0";
    private String description = "d0";
    private Double price = 0.0;
    private Integer duration = 0;

    @Override
    public GiftCertificateRequest build() {
        return GiftCertificateRequest.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
    }

}