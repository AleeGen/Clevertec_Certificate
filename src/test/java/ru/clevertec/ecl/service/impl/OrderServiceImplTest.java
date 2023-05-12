package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.filter.impl.OrderFilter;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.service.util.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.service.util.mapper.OrderMapper;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.service.util.mapper.UserMapper;
import ru.clevertec.ecl.util.builder.impl.dto.request.OrderReqBuilder;
import ru.clevertec.ecl.util.builder.impl.dto.response.GCResBuilder;
import ru.clevertec.ecl.util.builder.impl.dto.response.OrderResBuilder;
import ru.clevertec.ecl.util.builder.impl.dto.response.UserResBuilder;
import ru.clevertec.ecl.util.builder.impl.entity.GCBuilder;
import ru.clevertec.ecl.util.builder.impl.entity.OrderBuilder;
import ru.clevertec.ecl.util.builder.impl.entity.UserBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private GiftCertificateServiceImpl gcService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private GiftCertificateMapper gcMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private OrderRepository rep;
    @Mock
    private OrderMapper mapper;
    @Captor
    private ArgumentCaptor<Order> captor;

    @Nested
    class CheckFindAll {

        private static Page<Order> orderResponse;
        private static Pageable pageable;

        @BeforeAll
        static void init() {
            orderResponse = new PageImpl<>(List.of(
                    OrderBuilder.aOrder().build(),
                    OrderBuilder.aOrder().build(),
                    OrderBuilder.aOrder().build()));
            pageable = Pageable.ofSize(20).withPage(0);
        }


        @Test
        void shouldReturnExpectedSize() {
            int expected = orderResponse.getSize();
            doReturn(OrderBuilder.aOrder().withId(null).withCost(null).withDate(null).build())
                    .when(mapper).toFrom(any(OrderFilter.class));
            var filter = OrderFilter.builder().build();
            doReturn(orderResponse).when(rep).findAll(any(Example.class), any(Pageable.class));
            int actual = orderService.findAll(filter, pageable).size();
            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class CheckFindById {

        @Test
        void shouldThrowServiceException() {
            long id = -1L;
            doReturn(Optional.empty()).when(rep).findById(id);
            assertThrows(ServiceException.class, () -> orderService.findById(id));
        }

        @Test
        void shouldReturnExpected() {
            long id = 1L;
            var expected = OrderBuilder.aOrder().withId(id).build();
            doReturn(Optional.of(expected)).when(rep).findById(id);
            var response = OrderResBuilder.aOrderRes()
                    .withId(expected.getId())
                    .withCost(expected.getCost())
                    .withDate(expected.getDate())
                    .withUserId(expected.getUser().getId())
                    .withGcId(expected.getGc().getId())
                    .build();
            doReturn(response).when(mapper).toFrom(expected);
            var actual = orderService.findById(id);
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(expected.getId()),
                    () -> assertThat(actual.cost()).isEqualTo(expected.getCost()),
                    () -> assertThat(actual.date()).isEqualTo(expected.getDate()),
                    () -> assertThat(actual.userId()).isEqualTo(expected.getUser().getId()),
                    () -> assertThat(actual.gcId()).isEqualTo(expected.getGc().getId()));
        }

    }

    @Nested
    class CheckSave {

        @Test
        void shouldSave() {
            var request = OrderReqBuilder.aOrderReq().build();
            Long expectedUserId = request.userId();
            Long expectedGCId = request.gcId();
            doReturn(UserResBuilder.aUserRes().withId(expectedUserId).build()).when(userService).findById(expectedUserId);
            doReturn(UserBuilder.aUser().withId(expectedUserId).build()).when(userMapper).toFrom(any(UserResponse.class));
            doReturn(GCResBuilder.aGCRes().withId(expectedGCId).build()).when(gcService).findById(expectedGCId);
            doReturn(GCBuilder.aGC().withId(expectedGCId).build()).when(gcMapper).toFrom(any(GiftCertificateResponse.class));
            orderService.save(request);
            verify(rep).save(captor.capture());
            assertAll(
                    () -> assertThat(captor.getValue().getUser().getId()).isEqualTo(expectedUserId),
                    () -> assertThat(captor.getValue().getGc().getId()).isEqualTo(expectedGCId));
        }

    }

    @Nested
    class CheckUpdate {

        @Test
        void shouldThrowUnsupportedOperationException() {
            assertThrows(UnsupportedOperationException.class, () -> orderService.update(OrderReqBuilder.aOrderReq().build()));
        }

    }

    @Nested
    class CheckDelete {

        @Test
        void shouldThrowServiceException() {
            doReturn(Optional.empty()).when(rep).findById(-1L);
            assertThrows(ServiceException.class, () -> orderService.delete(-1L));
        }

        @Test
        void shouldDelete() {
            long id = 1L;
            var o = OrderBuilder.aOrder().withId(id).build();
            doReturn(Optional.of(o)).when(rep).findById(id);
            assertAll(() -> assertDoesNotThrow(() -> orderService.delete(id)),
                    () -> verify(rep).delete(o));
        }

    }

}