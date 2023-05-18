package ru.clevertec.ecl.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.data.builder.impl.dto.request.OrderReqBuilder;
import ru.clevertec.ecl.data.database.OrderDBResponse;
import ru.clevertec.ecl.exception.response.ServiceErrorResponse;
import ru.clevertec.ecl.integration.AbstractTestContainer;

import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
public class OrderIT extends AbstractTestContainer {

    private static final OrderDBResponse dataResp = new OrderDBResponse();
    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final String MAPPING = "/orders";

    @Nested
    class CheckGetAll {

        @Test
        void shouldReturnExpected() throws Exception {
            var response = mapper.writeValueAsString(dataResp.all());
            mockMvc.perform(get(MAPPING))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

        @Test
        void shouldReturnExpectedWithPageable() throws Exception {
            var response = mapper.writeValueAsString(dataResp.withPageable());
            mockMvc.perform(get(MAPPING + "?page=0&size=2&sort=cost,desc"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

        @Test
        void shouldReturnExpectedWithFilter() throws Exception {
            var response = mapper.writeValueAsString(dataResp.withFilter());
            mockMvc.perform(get(MAPPING + "?userId=3"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

        @Test
        void shouldReturnEmpty() throws Exception {
            long nonExistUserId = 99L;
            var response = mapper.writeValueAsString(dataResp.empty());
            mockMvc.perform(get(MAPPING + "?userId=" + nonExistUserId))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

    }

    @Nested
    class CheckById {

        private static Stream<Long> requestByIds() {
            return LongStream.range(1, dataResp.getSize() + 1).boxed();
        }

        @Test
        void shouldReturnError() throws Exception {
            var response = new ServiceErrorResponse();
            response.setMessage(String.format("Order with id = %d not found", 99));
            mockMvc.perform(get(MAPPING + "/99"))
                    .andExpect(status().is5xxServerError())
                    .andExpect(content().string(mapper.writeValueAsString(response)));
        }

        @ParameterizedTest
        @MethodSource("requestByIds")
        void shouldReturnExpected(Long id) throws Exception {
            var response = mapper.writeValueAsString(dataResp.byContent(id));
            mockMvc.perform(get(MAPPING + "/" + id))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

    }

    @Nested
    class CheckPost {

        private static Stream<Arguments> contentAndIncrementId() {
            return Stream.of(
                    Arguments.of(1L, 2L),
                    Arguments.of(2L, 3L),
                    Arguments.of(3L, 4L));
        }

        @ParameterizedTest
        @MethodSource("contentAndIncrementId")
        void shouldSaveAndReturnExpected(Long userId, Long gcId) throws Exception {
            var request = OrderReqBuilder.aOrderReq()
                    .withUserId(userId)
                    .withGcId(gcId)
                    .build();
            var response = dataResp.byContent(dataResp.getSize() + 1, userId, gcId);
            mockMvc.perform(post(MAPPING)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("id").value(response.id()),
                            jsonPath("cost").value(response.cost()),
                            jsonPath("date").exists(),
                            jsonPath("userId").value(response.userId()),
                            jsonPath("gcId").value(response.gcId()));
        }

    }

    @Nested
    class CheckPut {

        @Test
        void shouldReturnError() throws Exception {
            var request = OrderReqBuilder.aOrderReq().build();
            mockMvc.perform(put(MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().is5xxServerError());
        }

    }

    @Nested
    class CheckDelete {

        private static Stream<Long> deleteByIds() {
            return LongStream.range(1, dataResp.getSize() + 1).boxed();
        }

        @Test
        void shouldReturnError() throws Exception {
            long nonExistId = 99L;
            mockMvc.perform(delete(MAPPING + "/" + nonExistId))
                    .andExpect(status().is5xxServerError());
        }

        @ParameterizedTest
        @MethodSource("deleteByIds")
        void shouldDelete(Long id) throws Exception {
            mockMvc.perform(delete(MAPPING + "/" + id))
                    .andExpect(status().isNoContent());
        }

    }

}