package ru.clevertec.ecl.data.database;

import ru.clevertec.ecl.data.builder.impl.dto.response.OrderResBuilder;
import ru.clevertec.ecl.dto.response.OrderResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

public class OrderDBResponse {

    private final long sizeTable = 10L;

    public long getSize() {
        return sizeTable;
    }

    public List<OrderResponse> all() {
        return LongStream.range(1, sizeTable + 1).boxed().map(this::byContent).toList();
    }

    public List<OrderResponse> withPageable() {
        return List.of(byContent(10L), byContent(6L));
    }

    public List<OrderResponse> withFilter() {
        return LongStream.range(4, 7).boxed().map(this::byContent).toList();
    }

    public List<OrderResponse> empty() {
        return Collections.emptyList();
    }

    public OrderResponse byContent(Long id) {
        long userId = 0;
        long gcId = 0;
        switch (id.intValue()) {
            case 1 -> userId = 1L;
            case 2, 3 -> userId = 2L;
            case 4, 5, 6 -> userId = 3L;
            case 7, 8, 9, 10 -> userId = 4L;
        }
        switch (id.intValue()) {
            case 1, 2, 4, 7 -> gcId = 1L;
            case 3, 5, 8 -> gcId = 2L;
            case 6, 9 -> gcId = 3L;
            case 10 -> gcId = 4L;
        }
        double cost = Double.parseDouble(String.valueOf(gcId));
        return OrderResBuilder.aOrderRes()
                .withId(id)
                .withCost(cost)
                .withUserId(userId)
                .withGcId(gcId)
                .build();
    }

    public OrderResponse byContent(Long id, Long userId, Long gcId) {
        return OrderResponse.builder()
                .id(id)
                .cost(Double.parseDouble(String.valueOf(gcId)))
                .userId(userId)
                .gcId(gcId)
                .build();
    }

}