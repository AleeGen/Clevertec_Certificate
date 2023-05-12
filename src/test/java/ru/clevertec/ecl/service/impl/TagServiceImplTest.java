package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.filter.impl.TagFilter;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.util.mapper.TagMapper;
import ru.clevertec.ecl.util.builder.impl.dto.response.TagResBuilder;
import ru.clevertec.ecl.util.builder.impl.entity.TagBuilder;
import ru.clevertec.ecl.util.builder.impl.dto.request.TagReqBuilder;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl service;
    @Mock
    private TagRepository rep;
    @Mock
    private TagMapper mapper;
    @Captor
    private ArgumentCaptor<Tag> captor;

    @Nested
    class CheckFindAll {

        private static Page<Tag> tagResponse;
        private static Pageable pageable;

        @BeforeAll
        static void init() {
            tagResponse = new PageImpl<>(List.of(
                    TagBuilder.aTag().build(),
                    TagBuilder.aTag().build(),
                    TagBuilder.aTag().build()));
            pageable = Pageable.ofSize(20).withPage(0);
        }


        @Test
        void shouldReturnExpectedSize() {
            int expected = tagResponse.getSize();
            doReturn(TagBuilder.aTag().withId(null).withName(null).build())
                    .when(mapper).toFrom(any(TagFilter.class));
            var filter = TagFilter.builder().build();
            doReturn(tagResponse).when(rep).findAll(any(Example.class), any(Pageable.class));
            int actual = service.findAll(filter, pageable).size();
            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class CheckFindById {

        @Test
        void shouldThrowServiceException() {
            long id = -1L;
            doReturn(Optional.empty()).when(rep).findById(id);
            assertThrows(ServiceException.class, () -> service.findById(id));
        }

        @Test
        void shouldReturnExpected() {
            long id = 1L;
            var expected = TagBuilder.aTag().withId(id).build();
            doReturn(Optional.of(expected)).when(rep).findById(id);
            var response = TagResBuilder.aTagRes().withId(id).withName(expected.getName()).build();
            doReturn(response).when(mapper).toFrom(expected);
            var actual = service.findById(id);
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(expected.getId()),
                    () -> assertThat(actual.name()).isEqualTo(expected.getName()));
        }

    }

    @Nested
    class CheckSave {

        @Test
        void shouldSave() {
            var request = TagReqBuilder.aTagReq().build();
            String expected = request.name();
            Tag fromMapping = TagBuilder.aTag().withName(expected).build();
            doReturn(fromMapping).when(mapper).toFrom(request);
            service.save(request);
            verify(rep).save(captor.capture());
            String actual = captor.getValue().getName();
            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class CheckUpdate {

        @Test
        void shouldThrowServiceException() {
            var request = TagReqBuilder.aTagReq().withId(-1L).build();
            doReturn(Optional.empty()).when(rep).findById(request.id());
            assertThrows(ServiceException.class, () -> service.update(request));
        }

        @Test
        void shouldUpdate() {
            var request = TagReqBuilder.aTagReq().build();
            var tag = TagBuilder.aTag().withId(request.id()).build();
            doReturn(Optional.of(tag)).when(rep).findById(request.id());
            service.update(request);
            assertAll(
                    () -> assertThat(request.id()).isEqualTo(tag.getId()),
                    () -> assertThat(request.name()).isEqualTo(tag.getName()));
        }

    }

    @Nested
    class CheckDelete {

        @Test
        void shouldThrowServiceException() {
            doReturn(Optional.empty()).when(rep).findById(-1L);
            assertThrows(ServiceException.class, () -> service.delete(-1L));
        }

        @Test
        void shouldDelete() {
            long id = 1L;
            var tag = TagBuilder.aTag().withId(id).build();
            doReturn(Optional.of(tag)).when(rep).findById(id);
            assertAll(() -> assertDoesNotThrow(() -> service.delete(id)),
                    () -> verify(rep).delete(tag));
        }

    }

    @Nested
    class CheckFindByName {

        private static Stream<Arguments> args() {
            return Stream.of(
                    Arguments.of(TagBuilder.aTag().withName("n1").build()),
                    Arguments.of(TagBuilder.aTag().withName("n2").build()),
                    Arguments.of(TagBuilder.aTag().withName("n3").build()));
        }

        @ParameterizedTest
        @MethodSource("args")
        void shouldReturnExpected(Tag tag) {
            String expected = tag.getName();
            doReturn(tag).when(rep).findByName(expected);
            String actual = service.findByName(expected).getName();
            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class CheckFindMostUsedByUser {

        @Test
        void shouldReturnExpected() {
            Tag tag = TagBuilder.aTag().build();
            var expected = TagResBuilder.aTagRes().withId(tag.getId()).withName(tag.getName()).build();
            doReturn(tag).when(rep).findMostUsedByUser();
            doReturn(expected).when(mapper).toFrom(tag);
            var actual = service.findMostUsedByUser();
            assertThat(actual).isEqualTo(expected);
        }

    }

}