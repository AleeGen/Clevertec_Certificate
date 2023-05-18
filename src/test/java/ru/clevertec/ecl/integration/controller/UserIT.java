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
import ru.clevertec.ecl.data.builder.impl.dto.request.UserReqBuilder;
import ru.clevertec.ecl.data.database.UserDBResponse;
import ru.clevertec.ecl.exception.response.ServiceErrorResponse;
import ru.clevertec.ecl.integration.AbstractTestContainer;

import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RequiredArgsConstructor
public class UserIT extends AbstractTestContainer {

    private static final UserDBResponse dataResp = new UserDBResponse();
    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final String MAPPING = "/users";

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
            mockMvc.perform(get(MAPPING + "?page=1&size=2&sort=id,desc"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

        @Test
        void shouldReturnExpectedWithFilter() throws Exception {
            var response = mapper.writeValueAsString(dataResp.withFilter());
            mockMvc.perform(get(MAPPING + "?firstname=firstname1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

        @Test
        void shouldReturnEmpty() throws Exception {
            var response = mapper.writeValueAsString(dataResp.empty());
            mockMvc.perform(get(MAPPING + "?firstname=nonExistName"))
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
            response.setMessage(String.format("User with id = %d not found", 99));
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
                    Arguments.of("newFirstname1", "newLastname1"),
                    Arguments.of("newFirstname2", "newLastname2"),
                    Arguments.of("newFirstname3", "newLastname3"));
        }

        @ParameterizedTest
        @MethodSource("contentAndIncrementId")
        void shouldSaveAndReturnExpected(String fName, String lName) throws Exception {
            var request = UserReqBuilder.aUserReq()
                    .withId(null)
                    .withFirstname(fName)
                    .withLastname(lName)
                    .build();
            var response = dataResp.byContent(dataResp.getSize() + 1, fName, lName);
            mockMvc.perform(post(MAPPING)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("id").value(response.id()),
                            jsonPath("firstname").value(response.firstname()),
                            jsonPath("lastname").value(response.lastname()),
                            jsonPath("orders").isEmpty());
        }

    }

    @Nested
    class CheckPut {

        private static Stream<Long> nonExistId() {
            return Stream.of(null, dataResp.getSize() + 1);
        }

        @ParameterizedTest
        @MethodSource("nonExistId")
        void shouldReturnError(Long id) throws Exception {
            var request = UserReqBuilder.aUserReq().withId(id).build();
            mockMvc.perform(put(MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().is5xxServerError());
        }

        @Test
        void shouldUpdateAndReturnExpected() throws Exception {
            var request = UserReqBuilder.aUserReq()
                    .withId(1L)
                    .withFirstname("newFirstname")
                    .withLastname("newLastname")
                    .build();
            var response = mapper.writeValueAsString(dataResp.byContent(
                    request.id(),
                    request.firstname(),
                    request.lastname()));
            mockMvc.perform(put(MAPPING)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(response));
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