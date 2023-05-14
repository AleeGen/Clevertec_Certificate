package ru.clevertec.ecl.service.util.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.data.builder.impl.dto.request.filter.OrderFilterBuilder;
import ru.clevertec.ecl.data.builder.impl.dto.response.OrderResBuilder;
import ru.clevertec.ecl.data.builder.impl.entity.OrderBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMapperTest {

    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    @Test
    void checkEntityToResponse() {
        var expected = OrderResBuilder.aOrderRes().build();
        var entity = OrderBuilder.aOrder().build();
        var response = mapper.toFrom(entity);
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void checkResponseToEntity() {
        var expected = OrderBuilder.aOrder().build();
        var response = OrderResBuilder.aOrderRes().build();
        var entity = mapper.toFrom(response);
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void checkFilterToEntity() {
        var expected = OrderBuilder.aOrder().build();
        var filter = OrderFilterBuilder.aOrderFilter().build();
        var entity = mapper.toFrom(filter);
        assertThat(entity).isEqualTo(expected);
    }

}