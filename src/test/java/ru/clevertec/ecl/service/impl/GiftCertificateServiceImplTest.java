package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.request.filter.impl.GiftCertificateFilter;
import ru.clevertec.ecl.exception.UtilException;
import ru.clevertec.ecl.service.util.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.service.util.patch.PatchRequest;
import ru.clevertec.ecl.util.builder.impl.dto.request.GCReqBuilder;
import ru.clevertec.ecl.util.builder.impl.dto.response.GCResBuilder;
import ru.clevertec.ecl.util.builder.impl.entity.GCBuilder;
import ru.clevertec.ecl.util.builder.impl.entity.TagBuilder;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.repository.GiftCertificateRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl gcService;
    @Mock
    private GiftCertificateRepository gcRep;
    @Mock
    private TagServiceImpl tagService;
    @Mock
    private GiftCertificateMapper gcMapper;
    @Captor
    private ArgumentCaptor<GiftCertificate> captor;

    @Nested
    class CheckFindAll {
        private static Page<GiftCertificate> gcResponse;
        private static Pageable pageable;

        @BeforeAll
        static void init() {
            gcResponse = new PageImpl<>(List.of(
                    GCBuilder.aGC().build(),
                    GCBuilder.aGC().build(),
                    GCBuilder.aGC().build(),
                    GCBuilder.aGC().build(),
                    GCBuilder.aGC().build()));
            pageable = Pageable.ofSize(20).withPage(0);
        }

        private static Stream<Arguments> args() {
            return Stream.of(
                    Arguments.of("tagName", null),
                    Arguments.of(null, "part"),
                    Arguments.of("tagName", "part"));
        }

        @Test
        void shouldReturnExpectedSize() {
            int expected = 5;
            var filter = GiftCertificateFilter.builder().build();
            doReturn(GCBuilder.aGC().build()).when(gcMapper).toFrom(any(GiftCertificateFilter.class));
            doReturn(gcResponse).when(gcRep).findAll(any(Example.class), any(Pageable.class));
            doReturn(GCResBuilder.aGCRes().build()).when(gcMapper).toFrom(any(GiftCertificate.class));
            int actual = gcService.findAll(filter, pageable).size();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldReturnWithTags() {
            long expected = 15;
            var filter = GiftCertificateFilter.builder().build();
            doReturn(GCBuilder.aGC().build()).when(gcMapper).toFrom(any(GiftCertificateFilter.class));
            doReturn(gcResponse).when(gcRep).findAll(any(Example.class), any(Pageable.class));
            doReturn(GCResBuilder.aGCRes().build()).when(gcMapper).toFrom(any(GiftCertificate.class));
            var all = gcService.findAll(filter, pageable);
            long actual = all.stream().mapToLong(gc -> gc.tags().size()).sum();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldCallDefaultJPAMethod() {
            var filter = GiftCertificateFilter.builder().tagName(null).part(null).build();
            doReturn(GCBuilder.aGC().build()).when(gcMapper).toFrom(filter);
            doReturn(gcResponse).when(gcRep).findAll(any(Example.class), any(Pageable.class));
            gcService.findAll(filter, pageable);
            verify(gcRep).findAll(any(Example.class), any(Pageable.class));
        }

        @ParameterizedTest
        @MethodSource("args")
        void shouldCallCustomMethod(String tagName, String part) {
            var filter = GiftCertificateFilter.builder().tagName(tagName).part(part).build();
            gcService.findAll(filter, pageable);
            verify(gcRep).findAll(tagName, part, pageable);
        }

    }

    @Nested
    class CheckFindById {

        @Test
        void shouldThrowServiceException() {
            long id = -1L;
            doReturn(Optional.empty()).when(gcRep).findById(id);
            assertThrows(ServiceException.class, () -> gcService.findById(id));
        }

        @Test
        void shouldReturnExpected() {
            long id = 1L;
            var expected = GCBuilder.aGC().build();
            doReturn(Optional.of(expected)).when(gcRep).findById(id);
            doReturn(GCResBuilder.aGCRes().build()).when(gcMapper).toFrom(expected);
            var actual = gcService.findById(id);
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

        private final GiftCertificateRequest request = GCReqBuilder.aGCReq().build();

        @BeforeEach
        void setUp() {
            doReturn(GCBuilder.aGC().build()).when(gcMapper).toFrom(request);
        }

        @Test
        void shouldSave() {
            gcService.save(request);
            verify(gcRep).save(any());
        }

        @Test
        void shouldAddTagsToGC() {
            var expectedNameTags = new ArrayList<>(request.tags().stream().map(TagRequest::name).toList());
            expectedNameTags.add(request.name());
            expectedNameTags.forEach(name ->
                    doReturn(TagBuilder.aTag().withName(name).build())
                            .when(tagService).findByName(name));
            gcService.save(request);
            verify(gcRep).save(captor.capture());
            var actualNameTags = captor.getValue().getTags().stream().map(Tag::getName).toList();
            assertThat(actualNameTags).isEqualTo(expectedNameTags);
        }

        @Test
        void shouldSetDate() {
            var request = GCReqBuilder.aGCReq().build();
            gcService.save(request);
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
            GiftCertificateRequest request = GCReqBuilder.aGCReq().withId(-1L).build();
            doReturn(Optional.empty()).when(gcRep).findById(request.id());
            assertThrows(ServiceException.class, () -> gcService.update(request));
        }

        @Test
        void shouldCallFlush() {
            doReturn(Optional.of(GCBuilder.aGC().build())).when(gcRep).findById(any(Long.class));
            doReturn(GCBuilder.aGC().build()).when(gcMapper).toFrom(any(GiftCertificateRequest.class));
            gcService.update(GCReqBuilder.aGCReq().build());
            verify(gcRep).flush();
        }

        @Test
        void shouldAddTagsToGC() {
            var request = GCReqBuilder.aGCReq().build();
            var gc = GCBuilder.aGC().withTags(Collections.emptyList()).build();
            doReturn(Optional.of(gc)).when(gcRep).findById(request.id());
            var expectedNameTags = new ArrayList<>(request.tags().stream().map(TagRequest::name).toList());
            expectedNameTags.add(request.name());
            expectedNameTags.forEach(name ->
                    doReturn(TagBuilder.aTag().withName(name).build())
                            .when(tagService).findByName(name));
            doReturn(GCBuilder.aGC().build()).when(gcMapper).toFrom(request);
            gcService.update(request);
            var actualNameTags = gc.getTags().stream().map(Tag::getName).toList();
            assertThat(actualNameTags).isEqualTo(expectedNameTags);
        }


        @Test
        void shouldUpdate() {
            var request = GCReqBuilder.aGCReq().build();
            var actual = GCBuilder.aGC().build();
            var lastUpdateDate = actual.getLastUpdateDate();
            doReturn(Optional.of(actual)).when(gcRep).findById(request.id());
            doReturn(actual).when(gcMapper).toFrom(request);
            gcService.update(request);
            assertAll(
                    () -> verify(gcMapper).updateGC(actual, request),
                    () -> assertThat(actual.getLastUpdateDate()).isNotEqualTo(lastUpdateDate)
            );
        }

    }

    @Nested
    class CheckDelete {

        @Test
        void shouldThrowServiceException() {
            doReturn(Optional.empty()).when(gcRep).findById(-1L);
            assertThrows(ServiceException.class, () -> gcService.delete(-1L));
        }

        @Test
        void shouldDelete() {
            long id = 1L;
            var gc = GCBuilder.aGC().withId(id).build();
            doReturn(Optional.of(gc)).when(gcRep).findById(id);
            assertAll(
                    () -> assertDoesNotThrow(() -> gcService.delete(id)),
                    () -> verify(gcRep).delete(gc));
        }

    }

    @Nested
    class CheckPatch {

        @Test
        void shouldThrowServiceException() {
            doReturn(Optional.empty()).when(gcRep).findById(any(Long.class));
            assertThrows(ServiceException.class, () -> gcService.patch(-1L, new PatchRequest("", "")));
        }

        @Test
        void shouldThrowUtilExceptionWhenFieldNotExist() {
            long id = 0L;
            String nonExistField = "nonExistField";
            doReturn(Optional.of(GCBuilder.aGC().build())).when(gcRep).findById(id);
            assertThrows(UtilException.class, () -> gcService.patch(id, new PatchRequest(nonExistField, "")));
        }

        @Test
        void shouldThrowUtilExceptionWhenValueInvalid() {
            long id = 0L;
            String invalidValue = "";
            doReturn(Optional.of(GCBuilder.aGC().build())).when(gcRep).findById(id);
            assertThrows(UtilException.class, () -> gcService.patch(id, new PatchRequest("name", invalidValue)));
        }

        @Test
        void shouldCallFlush() {
            var gc = GCBuilder.aGC().build();
            doReturn(Optional.of(gc)).when(gcRep).findById(any(Long.class));
            var request = new PatchRequest("id", "0");
            gcService.patch(gc.getId(), request);
            verify(gcRep).flush();
        }

        @Test
        void shouldUpdateName() {
            var actual = GCBuilder.aGC().build();
            doReturn(Optional.of(actual)).when(gcRep).findById(actual.getId());
            var request = new PatchRequest("name", "\"newName\"");
            gcService.patch(actual.getId(), request);
            assertThat(actual.getName()).isEqualTo("newName");
        }

        @Test
        void shouldUpdateDescription() {
            var actual = GCBuilder.aGC().build();
            doReturn(Optional.of(actual)).when(gcRep).findById(actual.getId());
            var request = new PatchRequest("description", "\"newDescription\"");
            gcService.patch(actual.getId(), request);
            assertThat(actual.getDescription()).isEqualTo("newDescription");
        }

        @Test
        void shouldUpdatePrice() {
            var actual = GCBuilder.aGC().build();
            doReturn(Optional.of(actual)).when(gcRep).findById(actual.getId());
            var request = new PatchRequest("price", "4.5");
            gcService.patch(actual.getId(), request);
            assertThat(actual.getPrice()).isEqualTo(4.5);
        }

        @Test
        void shouldUpdateDuration() {
            var actual = GCBuilder.aGC().build();
            doReturn(Optional.of(actual)).when(gcRep).findById(actual.getId());
            var request = new PatchRequest("duration", "2");
            gcService.patch(actual.getId(), request);
            assertThat(actual.getDuration()).isEqualTo(2);
        }

        @Test
        void shouldCallGetActualTags() {
            String tagName = "tagName";
            var gc = GCBuilder.aGC().build();
            doReturn(Optional.of(gc)).when(gcRep).findById(any(Long.class));
            var request = new PatchRequest("tags", String.format("[{\"name\": \"%s\"}]", tagName));
            gcService.patch(gc.getId(), request);
            verify(tagService).findByName(tagName);
        }

    }

}