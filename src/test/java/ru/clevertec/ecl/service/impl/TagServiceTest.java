package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.builder.impl.TagBuilder;
import ru.clevertec.ecl.builder.impl.TagRequestBuilder;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.repository.impl.TagRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService service;
    @Mock
    private TagRepository tagRep;
    @Captor
    private ArgumentCaptor<Tag> captor;

    @Nested
    class CheckFindAll {

        private static List<Tag> tagResponse;

        @BeforeAll
        static void init() {
            tagResponse = List.of(
                    TagBuilder.aTag().build(),
                    TagBuilder.aTag().build(),
                    TagBuilder.aTag().build());
        }

        @Test
        void shouldReturnExpectedSize() {
            int expected = 3;
            doReturn(tagResponse).when(tagRep).findAll();
            int actual = service.findAll().size();
            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class CheckFindById {

        @Test
        void shouldThrowServiceException() {
            long id = -1L;
            doReturn(Optional.empty()).when(tagRep).findById(id);
            assertThrows(ServiceException.class, () -> service.findById(id));
        }

        @Test
        void shouldReturnExpected() {
            long id = 1L;
            var expected = TagBuilder.aTag().build();
            doReturn(Optional.of(expected)).when(tagRep).findById(id);
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
            var request = TagRequestBuilder.aTagRequest().build();
            String expected = request.name();
            service.save(request);
            verify(tagRep).save(captor.capture());
            String actual = captor.getValue().getName();
            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class CheckUpdate {

        @Test
        void shouldThrowServiceException() {
            TagRequest request = TagRequestBuilder.aTagRequest().withId(-1L).build();
            doReturn(Optional.empty()).when(tagRep).findById(request.id());
            assertThrows(ServiceException.class, () -> service.update(request));
        }

        @Test
        void shouldUpdate() {
            var request = TagRequestBuilder.aTagRequest().build();
            var tag = TagBuilder.aTag().withId(request.id()).build();
            doReturn(Optional.of(tag)).when(tagRep).findById(request.id());
            service.update(request);
            verify(tagRep).update(captor.capture());
            Tag updated = captor.getValue();
            assertThat(updated.getName()).isEqualTo(request.name());
        }

    }

    @Nested
    class CheckDelete {

        @Test
        void shouldThrowServiceException() {
            doReturn(Optional.empty()).when(tagRep).findById(-1L);
            assertThrows(ServiceException.class, () -> service.delete(-1L));
        }

        @Test
        void shouldDelete() {
            long id = 1L;
            var tag = TagBuilder.aTag().withId(id).build();
            doReturn(Optional.of(tag)).when(tagRep).findById(id);
            assertAll(() -> assertDoesNotThrow(() -> service.delete(id)),
                    () -> verify(tagRep).delete(tag));
        }

    }

}