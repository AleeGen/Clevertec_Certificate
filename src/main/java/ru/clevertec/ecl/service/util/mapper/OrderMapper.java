package ru.clevertec.ecl.service.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.dto.request.filter.impl.OrderFilter;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

@Mapper
public interface OrderMapper {

    @Mapping(target = "userId", source = "order.user.id")
    @Mapping(target = "gcId", source = "order.gc.id")
    OrderResponse toFrom(Order order);

    default Order toFrom(OrderResponse order) {
        return Order.builder()
                .id(order.id())
                .cost(order.cost())
                .date(order.date())
                .user(User.builder().id(order.userId()).build())
                .gc(GiftCertificate.builder().id(order.id()).build())
                .build();
    }

    default Order toFrom(OrderFilter order) {
        return Order.builder()
                .id(order.id())
                .cost(order.cost())
                .date(order.date())
                .user(User.builder().id(order.userId()).build())
                .gc(GiftCertificate.builder().id(order.id()).build())
                .build();
    }

}