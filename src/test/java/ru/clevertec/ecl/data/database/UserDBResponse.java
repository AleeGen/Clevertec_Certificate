package ru.clevertec.ecl.data.database;

import ru.clevertec.ecl.data.builder.impl.dto.response.OrderResBuilder;
import ru.clevertec.ecl.data.builder.impl.dto.response.UserResBuilder;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.dto.response.UserResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

public class UserDBResponse {

    private final long sizeTable = 4L;

    public long getSize() {
        return sizeTable;
    }

    public List<UserResponse> all() {
        return LongStream.range(1, sizeTable + 1).boxed().map(this::byContent).toList();
    }

    public List<UserResponse> withPageable() {
        return LongStream.range(2, 4).boxed().map(i -> byContent(sizeTable - i)).toList();
    }

    public List<UserResponse> withFilter() {
        return List.of(byContent(1L));
    }

    public List<UserResponse> empty() {
        return Collections.emptyList();
    }

    public UserResponse byContent(Long id) {
        return UserResBuilder.aUserRes()
                .withId(id)
                .withFirstname("firstname" + id)
                .withLastname("lastname" + id)
                .withOrders(ordersByIdUser(id))
                .build();
    }

    public UserResponse byContent(Long id, String fName, String lName) {
        return UserResBuilder.aUserRes()
                .withId(id)
                .withFirstname(fName)
                .withLastname(lName)
                .withOrders(ordersByIdUser(id))
                .build();
    }

    private List<OrderResponse> ordersByIdUser(Long id) {
        return LongStream.range(1, id + 1).boxed().map(i -> OrderResBuilder
                .aOrderRes()
                .withId(LongStream.range(0, id).sum() + i)
                .withCost(Double.valueOf(i))
                .withUserId(id)
                .withGcId(i)
                .build()).toList();
    }

}