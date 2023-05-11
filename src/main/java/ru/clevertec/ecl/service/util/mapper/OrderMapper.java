package ru.clevertec.ecl.service.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.dto.request.filter.impl.OrderFilter;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.entity.Order;

@Mapper
public interface OrderMapper {

    @Mapping(target = "userId", source = "order.user.id")
    @Mapping(target = "gcId", source = "order.gc.id")
    OrderResponse toFrom(Order order);

    Order toFrom(OrderFilter order);

}