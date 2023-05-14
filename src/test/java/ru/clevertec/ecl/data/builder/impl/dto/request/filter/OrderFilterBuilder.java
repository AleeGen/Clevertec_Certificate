package ru.clevertec.ecl.data.builder.impl.dto.request.filter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.dto.request.filter.impl.OrderFilter;
import ru.clevertec.ecl.data.builder.EntityBuilder;

import java.time.LocalDateTime;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrderFilter")
public class OrderFilterBuilder implements EntityBuilder<OrderFilter> {

    private Long id = 1L;
    private Double cost = 1.0;
    private LocalDateTime date = LocalDateTime.of(2023, 1, 1, 0, 0, 0, 0);
    private Long userId = 1L;
    private Long gcId = 1L;

    @Override
    public OrderFilter build() {
        return OrderFilter.builder()
                .id(id)
                .cost(cost)
                .date(date)
                .userId(userId)
                .gcId(gcId)
                .build();
    }

}