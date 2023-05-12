package ru.clevertec.ecl.util.builder.impl.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.util.builder.EntityBuilder;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrderReq")
public class OrderReqBuilder implements EntityBuilder<OrderRequest> {

    private Long userId = 0L;
    private Long gcId = 0L;

    @Override
    public OrderRequest build() {
        return OrderRequest.builder()
                .userId(userId)
                .gcId(gcId)
                .build();
    }

}