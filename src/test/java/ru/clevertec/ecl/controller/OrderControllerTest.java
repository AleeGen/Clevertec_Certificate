package ru.clevertec.ecl.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.dto.request.filter.impl.OrderFilter;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.exception.UtilException;
import ru.clevertec.ecl.service.impl.OrderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import ru.clevertec.ecl.util.builder.impl.dto.request.OrderReqBuilder;
import ru.clevertec.ecl.util.builder.impl.dto.response.OrderResBuilder;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    private static final String pathMethodSources = "ru.clevertec.ecl.controller.OrderControllerTest#";
    private static final String MAPPING = "/orders";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private OrderServiceImpl service;

    @Nested
    class CheckGetAll {

        private static MockHttpServletRequestBuilder get;

        @BeforeAll
        static void init() {
            get = get(MAPPING)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        void shouldReturnExpected() throws Exception {
            var response = List.of(
                    OrderResBuilder.aOrderRes().withId(1L).withUserId(1L).withGcId(1L).build(),
                    OrderResBuilder.aOrderRes().withId(2L).withUserId(2L).withGcId(2L).build());
            doReturn(response).when(service).findAll(any(OrderFilter.class), any(Pageable.class));
            mockMvc.perform(get)
                    .andExpect(status().isOk())
                    .andExpect(content().string(mapper.writeValueAsString(response)));
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "exception")
        void shouldReturnError(Class<? extends RuntimeException> exception) throws Exception {
            doThrow(exception).when(service).findAll(any(OrderFilter.class), any(Pageable.class));
            mockMvc.perform(get).andExpect(status().is5xxServerError());
        }

    }

    @Nested
    class CheckGetById {

        private static final Long id = 0L;
        private static MockHttpServletRequestBuilder get;

        @BeforeAll
        static void init() {
            get = get(MAPPING + "/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        void shouldReturnExpected() throws Exception {
            var response = OrderResBuilder.aOrderRes().withId(id).build();
            doReturn(response).when(service).findById(id);
            mockMvc.perform(get)
                    .andExpect(status().isOk())
                    .andExpect(content().string(mapper.writeValueAsString(response)));
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "exception")
        void shouldReturnError(Class<? extends RuntimeException> exception) throws Exception {
            doThrow(exception).when(service).findById(id);
            mockMvc.perform(get).andExpect(status().is5xxServerError());
        }

    }

    @Nested
    class CheckPost {

        private static MockHttpServletRequestBuilder post;

        @BeforeAll
        static void init() {
            post = post(MAPPING)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "negative")
        void shouldReturnStatusBadRequest(OrderRequest request) throws Exception {
            mockMvc.perform(post.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "positive")
        void shouldReturnStatusCreated(OrderRequest request) throws Exception {
            mockMvc.perform(post.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturnExpected() throws Exception {
            var request = OrderReqBuilder.aOrderReq().withUserId(1L).withGcId(1L).build();
            var response = OrderResBuilder.aOrderRes().build();
            doReturn(response).when(service).save(any(OrderRequest.class));
            mockMvc.perform(post.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(mapper.writeValueAsString(response)));
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "exception")
        void shouldReturnError(Class<? extends RuntimeException> exception) throws Exception {
            var request = OrderReqBuilder.aOrderReq().withUserId(1L).withGcId(1L).build();
            doThrow(exception).when(service).save(any(OrderRequest.class));
            mockMvc.perform(post.content(mapper.writeValueAsString(request)))
                    .andExpect(status().is5xxServerError());
        }

    }

    @Nested
    class CheckPut {

        private static MockHttpServletRequestBuilder put;

        @BeforeAll
        static void init() {
            put = put(MAPPING)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "negative")
        void shouldReturnStatusBadRequest(OrderRequest request) throws Exception {
            mockMvc.perform(put.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "positive")
        void shouldReturnStatusCreated(OrderRequest request) throws Exception {
            mockMvc.perform(put.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturnExpected() throws Exception {
            var request = OrderReqBuilder.aOrderReq().withUserId(1L).withGcId(1L).build();
            var response = OrderResBuilder.aOrderRes().build();
            doReturn(response).when(service).update(any(OrderRequest.class));
            mockMvc.perform(put.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(mapper.writeValueAsString(response)));
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "exception")
        void shouldReturnError(Class<? extends RuntimeException> exception) throws Exception {
            var request = OrderReqBuilder.aOrderReq().withUserId(1L).withGcId(1L).build();
            doThrow(exception).when(service).update(any(OrderRequest.class));
            mockMvc.perform(put.content(mapper.writeValueAsString(request)))
                    .andExpect(status().is5xxServerError());
        }

    }

    @Nested
    class CheckDelete {

        private static final Long id = 0L;
        private static MockHttpServletRequestBuilder delete;

        @BeforeAll
        static void init() {
            delete = delete(MAPPING + "/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        void shouldReturnExpected() throws Exception {
            doNothing().when(service).delete(id);
            mockMvc.perform(delete).andExpect(status().isNoContent());
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "exception")
        void shouldReturnError(Class<? extends RuntimeException> exception) throws Exception {
            doThrow(exception).when(service).delete(id);
            mockMvc.perform(delete).andExpect(status().is5xxServerError());
        }

    }

    private static Stream<OrderRequest> negative() {
        return Stream.of(
                OrderReqBuilder.aOrderReq().withUserId(null).withGcId(1L).build(),
                OrderReqBuilder.aOrderReq().withUserId(-1L).withGcId(1L).build(),
                OrderReqBuilder.aOrderReq().withUserId(0L).withGcId(1L).build(),
                OrderReqBuilder.aOrderReq().withUserId(1L).withGcId(null).build(),
                OrderReqBuilder.aOrderReq().withUserId(1L).withUserId(-1L).build(),
                OrderReqBuilder.aOrderReq().withUserId(1L).withGcId(0L).build());
    }

    private static Stream<OrderRequest> positive() {
        return Stream.of(
                OrderReqBuilder.aOrderReq().withUserId(1L).withGcId(1L).build(),
                OrderReqBuilder.aOrderReq().withUserId(Long.MAX_VALUE).withGcId(Long.MAX_VALUE).build());
    }

    private static Stream<Class<?>> exception() {
        return Stream.of(
                ServiceException.class,
                UtilException.class);
    }

}