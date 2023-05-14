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
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.request.filter.impl.GiftCertificateFilter;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.exception.UtilException;
import ru.clevertec.ecl.service.impl.GiftCertificateServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import ru.clevertec.ecl.service.util.patch.PatchRequest;
import ru.clevertec.ecl.data.builder.impl.dto.request.GCReqBuilder;
import ru.clevertec.ecl.data.builder.impl.dto.response.GCResBuilder;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GiftCertificateControllerTest {

    private static final String pathMethodSources = "ru.clevertec.ecl.controller.GiftCertificateControllerTest#";
    private static final String MAPPING = "/gift-certificates";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GiftCertificateServiceImpl service;

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
                    GCResBuilder.aGCRes().withId(1L).withName("n1").build(),
                    GCResBuilder.aGCRes().withId(2L).withName("n2").build());
            doReturn(response).when(service).findAll(any(GiftCertificateFilter.class), any(Pageable.class));
            mockMvc.perform(get)
                    .andExpect(status().isOk())
                    .andExpect(content().string(mapper.writeValueAsString(response)));
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "exception")
        void shouldReturnError(Class<? extends RuntimeException> exception) throws Exception {
            doThrow(exception).when(service).findAll(any(GiftCertificateFilter.class), any(Pageable.class));
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
            var response = GCResBuilder.aGCRes().withId(id).build();
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
        @MethodSource(pathMethodSources + "negativeRequest")
        void shouldReturnStatusBadRequest(GiftCertificateRequest request) throws Exception {
            mockMvc.perform(post.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "positiveRequest")
        void shouldReturnStatusCreated(GiftCertificateRequest request) throws Exception {
            mockMvc.perform(post.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturnExpected() throws Exception {
            var request = GCReqBuilder.aGCReq().withId(1L).withPrice(1.0).withDuration(1).build();
            var response = GCResBuilder.aGCRes().build();
            doReturn(response).when(service).save(any(GiftCertificateRequest.class));
            mockMvc.perform(post.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(mapper.writeValueAsString(response)));
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "exception")
        void shouldReturnError(Class<? extends RuntimeException> exception) throws Exception {
            var request = GCReqBuilder.aGCReq().withId(1L).withPrice(1.0).withDuration(1).build();
            doThrow(exception).when(service).save(any(GiftCertificateRequest.class));
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
        @MethodSource(pathMethodSources + "negativeRequest")
        void shouldReturnStatusBadRequest(GiftCertificateRequest request) throws Exception {
            mockMvc.perform(put.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "positiveRequest")
        void shouldReturnStatusCreated(GiftCertificateRequest request) throws Exception {
            mockMvc.perform(put.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturnExpected() throws Exception {
            var request = GCReqBuilder.aGCReq().withId(1L).withPrice(1.0).withDuration(1).build();
            var response = GCResBuilder.aGCRes().build();
            doReturn(response).when(service).update(any(GiftCertificateRequest.class));
            mockMvc.perform(put.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(mapper.writeValueAsString(response)));
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "exception")
        void shouldReturnError(Class<? extends RuntimeException> exception) throws Exception {
            var request = GCReqBuilder.aGCReq().withId(1L).withPrice(1.0).withDuration(1).build();
            doThrow(exception).when(service).update(any(GiftCertificateRequest.class));
            mockMvc.perform(put.content(mapper.writeValueAsString(request)))
                    .andExpect(status().is5xxServerError());
        }

    }

    @Nested
    class CheckPatch {

        private static final Long id = 1L;
        private static MockHttpServletRequestBuilder patch;

        @BeforeAll
        static void init() {
            patch = patch(MAPPING + "/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "negativePatchRequest")
        void shouldReturnStatusBadRequest(PatchRequest request) throws Exception {
            mockMvc.perform(patch.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "positivePatchRequest")
        void shouldReturnStatusCreated(PatchRequest request) throws Exception {
            mockMvc.perform(patch.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturnExpected() throws Exception {
            var request = new PatchRequest("field", "value");
            var response = GCResBuilder.aGCRes().build();
            doReturn(response).when(service).patch(any(Long.class), any(PatchRequest.class));
            mockMvc.perform(patch.content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(mapper.writeValueAsString(response)));
        }

        @ParameterizedTest
        @MethodSource(pathMethodSources + "exception")
        void shouldReturnError(Class<? extends RuntimeException> exception) throws Exception {
            var request = new PatchRequest("field", "value");
            doThrow(exception).when(service).patch(any(Long.class), any(PatchRequest.class));
            mockMvc.perform(patch.content(mapper.writeValueAsString(request)))
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

    private static Stream<GiftCertificateRequest> negativeRequest() {
        var charactersMore30 = new StringBuilder();
        IntStream.range(0, 31).forEach(i -> charactersMore30.append("."));
        var charactersMore100 = new StringBuilder();
        IntStream.range(0, 101).forEach(i -> charactersMore100.append("."));
        return Stream.of(
                GCReqBuilder.aGCReq().withName(null).build(),
                GCReqBuilder.aGCReq().withName("").build(),
                GCReqBuilder.aGCReq().withName(" ").build(),
                GCReqBuilder.aGCReq().withName(charactersMore30.toString()).build(),
                GCReqBuilder.aGCReq().withDescription(null).build(),
                GCReqBuilder.aGCReq().withDescription("").build(),
                GCReqBuilder.aGCReq().withDescription(" ").build(),
                GCReqBuilder.aGCReq().withDescription(charactersMore100.toString()).build(),
                GCReqBuilder.aGCReq().withPrice(null).build(),
                GCReqBuilder.aGCReq().withPrice(-1.0).build(),
                GCReqBuilder.aGCReq().withPrice(0.0).build(),
                GCReqBuilder.aGCReq().withDuration(null).build(),
                GCReqBuilder.aGCReq().withDuration(-1).build(),
                GCReqBuilder.aGCReq().withDuration(0).build());
    }

    private static Stream<GiftCertificateRequest> positiveRequest() {
        var characters30 = new StringBuilder();
        IntStream.range(0, 30).forEach(i -> characters30.append("."));
        var characters100 = new StringBuilder();
        IntStream.range(0, 100).forEach(i -> characters100.append("."));
        return Stream.of(
                GCReqBuilder.aGCReq().withName("a").build(),
                GCReqBuilder.aGCReq().withName(characters30.toString()).build(),
                GCReqBuilder.aGCReq().withDescription("a").build(),
                GCReqBuilder.aGCReq().withDescription(characters100.toString()).build(),
                GCReqBuilder.aGCReq().withPrice(1.0).build(),
                GCReqBuilder.aGCReq().withPrice(Double.MAX_VALUE).build(),
                GCReqBuilder.aGCReq().withDuration(1).build(),
                GCReqBuilder.aGCReq().withDuration(Integer.MAX_VALUE).build());
    }

    private static Stream<PatchRequest> negativePatchRequest() {
        return Stream.of(
                new PatchRequest(null, null),
                new PatchRequest("", ""),
                new PatchRequest(" ", " "));
    }

    private static Stream<PatchRequest> positivePatchRequest() {
        return Stream.of(
                new PatchRequest("a", "a"),
                new PatchRequest("qwerty12345", "qwerty12345"));
    }

    private static Stream<Class<?>> exception() {
        return Stream.of(
                ServiceException.class,
                UtilException.class);
    }

}