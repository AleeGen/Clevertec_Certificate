package ru.clevertec.ecl.util.builder.impl.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.util.builder.EntityBuilder;

import java.time.LocalDateTime;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrderRes")
public class OrderResBuilder implements EntityBuilder<OrderResponse> {

    private Long id = 1L;
    private Double cost = 1.0;
    private LocalDateTime date = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
    private Long userId = 1L;
    private Long gcId = 1L;

    @Override
    public OrderResponse build() {
        return OrderResponse.builder()
                .id(id)
                .cost(cost)
                .date(date)
                .userId(userId)
                .gcId(gcId)
                .build();
    }

}