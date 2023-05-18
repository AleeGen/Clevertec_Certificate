package ru.clevertec.ecl.service.util.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.data.builder.impl.dto.request.UserReqBuilder;
import ru.clevertec.ecl.data.builder.impl.dto.response.UserResBuilder;
import ru.clevertec.ecl.data.builder.impl.entity.UserBuilder;
import ru.clevertec.ecl.data.builder.impl.dto.request.filter.UserFilterBuilder;

import java.util.Collections;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper mapperUser = Mappers.getMapper(UserMapper.class);
    @Mock
    private OrderMapper mapperOrder;

    @Test
    void checkRequestToEntity() {
        var expected = UserBuilder.aUser().withOrders(Collections.emptyList()).build();
        var request = UserReqBuilder.aUserReq().build();
        var entity = mapperUser.toFrom(request);
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void checkEntityToResponse() {
        var ordersEnt = UserBuilder.aUser().build().getOrders();
        var ordersRes = UserResBuilder.aUserRes().build().orders();
        IntStream.range(0, ordersEnt.size()).forEach(i ->
                doReturn(ordersRes.get(i))
                        .when(mapperOrder)
                        .toFrom(ordersEnt.get(i)));
        var expected = UserResBuilder.aUserRes().withOrders(ordersRes).build();
        var entity = UserBuilder.aUser().withOrders(ordersEnt).build();
        var response = mapperUser.toFrom(entity);
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void checkResponseToEntity() {
        var ordersEnt = UserBuilder.aUser().build().getOrders();
        var ordersRes = UserResBuilder.aUserRes().build().orders();
        IntStream.range(0, ordersEnt.size()).forEach(i ->
                doReturn(ordersEnt.get(i))
                        .when(mapperOrder)
                        .toFrom(ordersRes.get(i)));
        var expected = UserBuilder.aUser().withOrders(ordersEnt).build();
        var response = UserResBuilder.aUserRes().withOrders(ordersRes).build();
        var entity = mapperUser.toFrom(response);
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void checkFilterToEntity() {
        var expected = UserBuilder.aUser().withOrders(Collections.emptyList()).build();
        var filter = UserFilterBuilder.aUserFilter().build();
        var entity = mapperUser.toFrom(filter);
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void checkUpdateGC() {
        var expected = UserBuilder.aUser()
                .withFirstname("afterFirstname")
                .withLastname("afterLastName")
                .build();
        var entity = UserBuilder.aUser().build();
        var request = UserReqBuilder.aUserReq()
                .withFirstname("afterFirstname")
                .withLastname("afterLastName")
                .build();
        mapperUser.updateUser(entity, request);
        assertThat(entity).isEqualTo(expected);
    }

}