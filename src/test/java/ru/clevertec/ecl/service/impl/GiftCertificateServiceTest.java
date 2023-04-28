package ru.clevertec.ecl.service.impl;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.builder.impl.GCBuilder;
import ru.clevertec.ecl.builder.impl.GCRequestBuilder;
import ru.clevertec.ecl.builder.impl.TagBuilder;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.repository.impl.GiftCertificateRepository;
import ru.clevertec.ecl.repository.impl.TagRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {

    @InjectMocks
    private GiftCertificateService service;
    @Mock
    private GiftCertificateRepository gcRep;
    @Mock
    private TagRepository tagRep;
    @Captor
    private ArgumentCaptor<GiftCertificate> captor;

    @Nested
    class CheckFindAll {
        private static List<GiftCertificate> gcResponse;

        @BeforeAll
        static void init() {
            gcResponse = List.of(
                    GCBuilder.aGC().build(),
                    GCBuilder.aGC().build(),
                    GCBuilder.aGC().build(),
                    GCBuilder.aGC().build(),
                    GCBuilder.aGC().build());
        }

        @Test
        void shouldReturnExpectedSize() {
            int expected = 5;
            doReturn(gcResponse).when(gcRep).findAll();
            int actual = service.findAll().size();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldReturnWithTags() {
            long expected = 15;
            doReturn(gcResponse).when(gcRep).findAll();
            List<GiftCertificateResponse> all = service.findAll();
            long actual = all.stream().mapToLong(gc -> gc.tags().size()).sum();
            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class CheckFindById {

        @Test
        void shouldThrowServiceException() {
            long id = -1L;
            doReturn(Optional.empty()).when(gcRep).findById(id);
            assertThrows(ServiceException.class, () -> service.findById(id));
        }

        @Test
        void shouldReturnExpected() {
            long id = 1L;
            var expected = GCBuilder.aGC().build();
            doReturn(Optional.of(expected)).when(gcRep).findById(id);
            var actual = service.findById(id);
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(expected.getId()),
                    () -> assertThat(actual.name()).isEqualTo(expected.getName()),
                    () -> assertThat(actual.description()).isEqualTo(expected.getDescription()),
                    () -> assertThat(actual.price()).isEqualTo(expected.getPrice()),
                    () -> assertThat(actual.duration()).isEqualTo(expected.getDuration()),
                    () -> assertThat(actual.createDate()).isEqualTo(expected.getCreateDate()),
                    () -> assertThat(actual.lastUpdateDate()).isEqualTo(expected.getLastUpdateDate()));
        }

    }

    @Nested
    class CheckSave {

        @Test
        void shouldSave() {
            var request = GCRequestBuilder.aGCRequest().build();
            service.save(request);
            verify(gcRep).save(any());
        }

        @Test
        void shouldAddTagToGC() {
            var request = GCRequestBuilder.aGCRequest().build();
            String name = request.name();
            Tag tag = TagBuilder.aTag().withName(name).build();
            doReturn(tag).when(tagRep).findByName(any());
            service.save(request);
            verify(gcRep).save(captor.capture());
            List<Tag> tags = captor.getValue().getTags();
            boolean actual = tags.stream().anyMatch(t -> name.equals(t.getName()));
            assertTrue(actual);
        }

        @Test
        void shouldSetDate() {
            var request = GCRequestBuilder.aGCRequest().build();
            service.save(request);
            verify(gcRep).save(captor.capture());
            var createDate = captor.getValue().getCreateDate();
            var lastUpdateDate = captor.getValue().getLastUpdateDate();
            assertAll(
                    () -> assertThat(createDate).isNotNull(),
                    () -> assertThat(lastUpdateDate).isNotNull());
        }

    }

    @Nested
    class CheckUpdate {

        @Test
        void shouldThrowServiceException() {
            GiftCertificateRequest request = GCRequestBuilder.aGCRequest().withId(-1L).build();
            doReturn(Optional.empty()).when(gcRep).findById(request.id());
            assertThrows(ServiceException.class, () -> service.update(request));
        }

        @Test
        void shouldAddTagToGC() {
            var request = GCRequestBuilder.aGCRequest().build();
            var gc = GCBuilder.aGC().build();
            var tag = TagBuilder.aTag().build();
            doReturn(Optional.of(gc)).when(gcRep).findById(request.id());
            doReturn(tag).when(tagRep).findByName(request.name());
            service.update(request);
            verify(gcRep).update(captor.capture());
            List<Tag> tags = captor.getValue().getTags();
            assertTrue(tags.contains(tag));
        }

        @Test
        void shouldUpdate() {
            var request = GCRequestBuilder.aGCRequest().build();
            var gc = GCBuilder.aGC().withId(request.id()).build();
            var lastUpdateDate = gc.getLastUpdateDate();
            doReturn(Optional.of(gc)).when(gcRep).findById(request.id());
            service.update(request);
            verify(gcRep).update(captor.capture());
            GiftCertificate updated = captor.getValue();
            assertAll(
                    () -> assertThat(updated.getName()).isEqualTo(request.name()),
                    () -> assertThat(updated.getDescription()).isEqualTo(request.description()),
                    () -> assertThat(updated.getPrice()).isEqualTo(request.price()),
                    () -> assertThat(updated.getDuration()).isEqualTo(request.duration()),
                    () -> assertThat(updated.getLastUpdateDate()).isNotEqualTo(lastUpdateDate));
        }

    }

    @Nested
    class CheckDelete {

        @Test
        void shouldThrowServiceException() {
            doReturn(Optional.empty()).when(gcRep).findById(-1L);
            assertThrows(ServiceException.class, () -> service.delete(-1L));
        }

        @Test
        void shouldDelete() {
            long id = 1L;
            var gc = GCBuilder.aGC().withId(id).build();
            doReturn(Optional.of(gc)).when(gcRep).findById(id);
            assertAll(() -> assertDoesNotThrow(() -> service.delete(id)),
                    () -> verify(gcRep).delete(gc));
        }

    }

    @Nested
    class CheckFindBy {

        @Captor
        private ArgumentCaptor<Boolean> captorSortType;
        @Captor
        private ArgumentCaptor<String[]> captorSortBy;

        private static Stream<Arguments> argsSortType() {
            return Stream.of(
                    Arguments.of(null, false),
                    Arguments.of("", false),
                    Arguments.of("qwerty", false),
                    Arguments.of("asc", false),
                    Arguments.of("desc", true));
        }

        private static Stream<Arguments> argsSortBy() {
            return Stream.of(
                    Arguments.of(null, 0),
                    Arguments.of(new String[0], 0),
                    Arguments.of(new String[]{"id"}, 1),
                    Arguments.of(new String[]{"id", "name"}, 2));
        }

        @Test
        void tagNameShouldThrowServiceException() {
            String[] names = Arrays.array("f1", "f2");
            assertThrows(ServiceException.class, () -> service.findByTagName(null, null, names));
        }

        @ParameterizedTest(name = "{index} : {0}")
        @MethodSource(value = "argsSortType")
        void tagNameShouldCallRepositoryWithExpectedTypeSort(String sortType, boolean expected) {
            service.findByTagName("", sortType, "id");
            verify(gcRep).findByTagName(any(String.class), captorSortType.capture(), any(String[].class));
            boolean actual = captorSortType.getValue();
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest(name = "{index} : {0}")
        @MethodSource(value = "argsSortBy")
        void tagNameShouldCallRepository(String[] sortBy, int expected) {
            service.findByTagName(null, null, sortBy);
            verify(gcRep).findByTagName(any(), any(Boolean.class), captorSortBy.capture());
            int actual = captorSortBy.getValue().length;
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void partShouldThrowServiceException() {
            String[] names = Arrays.array("f1", "f2");
            assertThrows(ServiceException.class, () -> service.findByPart(null, null, names));
        }

        @ParameterizedTest(name = "{index} : {0}")
        @MethodSource(value = "argsSortType")
        void partShouldCallRepositoryWithExpectedTypeSort(String sortType, boolean expected) {
            service.findByPart("", sortType, "id");
            verify(gcRep).findByPart(any(String.class), captorSortType.capture(), any(String[].class));
            boolean actual = captorSortType.getValue();
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest(name = "{index} : {0}")
        @MethodSource(value = "argsSortBy")
        void partShouldCallRepository(String[] sortBy, int expected) {
            service.findByPart(null, null, sortBy);
            verify(gcRep).findByPart(any(), any(Boolean.class), captorSortBy.capture());
            int actual = captorSortBy.getValue().length;
            assertThat(actual).isEqualTo(expected);
        }

    }

}