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
import ru.clevertec.ecl.data.builder.impl.dto.request.GCReqBuilder;
import ru.clevertec.ecl.data.builder.impl.dto.request.TagReqBuilder;
import ru.clevertec.ecl.data.database.GiftCertificateDBResponse;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.exception.response.ServiceErrorResponse;
import ru.clevertec.ecl.integration.AbstractTestContainer;
import ru.clevertec.ecl.service.util.patch.PatchRequest;

import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
public class GiftCertificateIT extends AbstractTestContainer {

    private static final GiftCertificateDBResponse dataResp = new GiftCertificateDBResponse();
    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final String MAPPING = "/gift-certificates";

    @Nested
    class CheckGetAll {

        @Test
        void shouldReturnExpected() throws Exception {
            var response = mapper.writeValueAsString(dataResp.all());
            System.out.println(response);
            mockMvc.perform(get(MAPPING))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

        @Test
        void shouldReturnExpectedWithPageable() throws Exception {
            var response = mapper.writeValueAsString(dataResp.withPageable());
            mockMvc.perform(get(MAPPING + "?page=1&size=2&sort=lastUpdateDate,desc"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

        @Test
        void shouldReturnExpectedWithFilterDefaultParameters() throws Exception {
            var response = mapper.writeValueAsString(dataResp.withFilterDefaultParameters());
            mockMvc.perform(get(MAPPING + "?description=d4"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

        @Test
        void shouldReturnExpectedWithFilterCustomParameters() throws Exception {
            var response = mapper.writeValueAsString(dataResp.withFilterCustomParameters());
            mockMvc.perform(get(MAPPING + "?tagName=n1&part=d"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(response));
        }

        @Test
        void shouldReturnEmpty() throws Exception {
            var response = mapper.writeValueAsString(dataResp.empty());
            mockMvc.perform(get(MAPPING + "?name=nonExistName"))
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
            response.setMessage(String.format("Gift certificate with id = %d not found", 99));
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

        @Test
        void shouldSaveAndReturnExpected() throws Exception {
            var request = GCReqBuilder.aGCReq()
                    .withId(null)
                    .withName("newName")
                    .withDescription("newDescription")
                    .withPrice(11.1)
                    .withDuration(11)
                    .withTags(List.of(
                            TagReqBuilder.aTagReq().withId(null).withName("n1").build(),
                            TagReqBuilder.aTagReq().withId(null).withName("newTagName").build()))
                    .build();
            var expected = dataResp.postOrPut();
            var response = mockMvc.perform(post(MAPPING)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var actual = mapper.readValue(response, GiftCertificateResponse.class);
            assertAll(
                    () -> assertThat(actual.name()).isEqualTo(expected.name()),
                    () -> assertThat(actual.description()).isEqualTo(expected.description()),
                    () -> assertThat(actual.price()).isEqualTo(expected.price()),
                    () -> assertThat(actual.duration()).isEqualTo(expected.duration()),
                    () -> assertThat(actual.tags()).isEqualTo(expected.tags()));
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
            var request = GCReqBuilder.aGCReq().withId(id).build();
            mockMvc.perform(put(MAPPING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().is5xxServerError());
        }

        @Test
        void shouldUpdateAndReturnExpected() throws Exception {
            var request = GCReqBuilder.aGCReq()
                    .withId(1L)
                    .withName("newName")
                    .withDescription("newDescription")
                    .withPrice(11.1)
                    .withDuration(11)
                    .withTags(List.of(
                            TagReqBuilder.aTagReq().withId(null).withName("n1").build(),
                            TagReqBuilder.aTagReq().withId(null).withName("newTagName").build()))
                    .build();
            var expected = dataResp.postOrPut();
            var response = mockMvc.perform(post(MAPPING)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var actual = mapper.readValue(response, GiftCertificateResponse.class);
            assertAll(
                    () -> assertThat(actual.name()).isEqualTo(expected.name()),
                    () -> assertThat(actual.description()).isEqualTo(expected.description()),
                    () -> assertThat(actual.price()).isEqualTo(expected.price()),
                    () -> assertThat(actual.duration()).isEqualTo(expected.duration()),
                    () -> assertThat(actual.tags()).isEqualTo(expected.tags()));
        }

    }

    @Nested
    class CheckPatch {

        private static Stream<Arguments> patchField() {
            return Stream.of(
                    Arguments.of(1L, "name", "\"newName\""),
                    Arguments.of(2L, "description", "\"newDescription\""),
                    Arguments.of(3L, "price", "99.9"),
                    Arguments.of(4L, "duration", "99"));
        }

        @ParameterizedTest
        @MethodSource("patchField")
        void shouldUpdateField(Long id, String field, String value) throws Exception {
            var request = new PatchRequest(field, value);
            var expected = mapper.readValue(value, String.class);
            mockMvc.perform(patch(MAPPING + "/" + id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath(field).value(expected));

        }

        @Test
        void shouldUpdateTags() throws Exception {
            var request = new PatchRequest("tags", "[{\"name\":\"newTagName1\"},{\"name\":\"newTagName2\"}]");
            var expected = dataResp.patchTags();
            var content = mockMvc.perform(patch(MAPPING + "/" + 1)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andReturn().getResponse().getContentAsString();
            var actual = mapper.readValue(content, GiftCertificateResponse.class);
            assertThat(actual.tags()).isEqualTo(expected);
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