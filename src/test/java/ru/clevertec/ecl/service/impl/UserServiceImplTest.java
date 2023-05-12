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
import ru.clevertec.ecl.dto.request.filter.impl.UserFilter;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.util.mapper.UserMapper;
import ru.clevertec.ecl.exception.ServiceException;
import ru.clevertec.ecl.util.builder.impl.dto.request.UserReqBuilder;
import ru.clevertec.ecl.util.builder.impl.dto.response.UserResBuilder;
import ru.clevertec.ecl.util.builder.impl.entity.UserBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final static UserReqBuilder userReq = UserReqBuilder.aUserReq();
    @InjectMocks
    private UserServiceImpl service;
    @Mock
    private UserRepository rep;
    @Mock
    private UserMapper mapper;
    @Captor
    private ArgumentCaptor<User> captor;

    @Nested
    class CheckFindAll {

        private static Page<User> userResponse;
        private static Pageable pageable;

        @BeforeAll
        static void init() {
            userResponse = new PageImpl<>(List.of(
                    UserBuilder.aUser().build(),
                    UserBuilder.aUser().build(),
                    UserBuilder.aUser().build()));
            pageable = Pageable.ofSize(20).withPage(0);
        }


        @Test
        void shouldReturnExpectedSize() {
            int expected = userResponse.getSize();
            doReturn(UserBuilder.aUser().withId(null).withFirstname(null).withLastname(null).build())
                    .when(mapper).toFrom(any(UserFilter.class));
            var filter = UserFilter.builder().build();
            doReturn(userResponse).when(rep).findAll(any(Example.class), any(Pageable.class));
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
            var expected = UserBuilder.aUser().withId(id).build();
            doReturn(Optional.of(expected)).when(rep).findById(id);
            var response = UserResBuilder.aUserRes().withId(id)
                    .withFirstname(expected.getFirstname())
                    .withLastname(expected.getLastname())
                    .build();
            doReturn(response).when(mapper).toFrom(expected);
            var actual = service.findById(id);
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(expected.getId()),
                    () -> assertThat(actual.firstname()).isEqualTo(expected.getFirstname()),
                    () -> assertThat(actual.lastname()).isEqualTo(expected.getLastname()));
        }

    }

    @Nested
    class CheckSave {

        @Test
        void shouldSave() {
            var request = userReq.build();
            String expectedFName = request.firstname();
            String expectedLName = request.firstname();
            User fromMapping = UserBuilder.aUser().withFirstname(expectedFName).withLastname(expectedLName).build();
            doReturn(fromMapping).when(mapper).toFrom(request);
            service.save(request);
            verify(rep).save(captor.capture());
            assertAll(
                    () -> assertThat(captor.getValue().getFirstname()).isEqualTo(expectedFName),
                    () -> assertThat(captor.getValue().getLastname()).isEqualTo(expectedLName));
        }

    }

    @Nested
    class CheckUpdate {

        @Test
        void shouldThrowServiceException() {
            var request = userReq.withId(-1L).build();
            doReturn(Optional.empty()).when(rep).findById(request.id());
            assertThrows(ServiceException.class, () -> service.update(request));
        }

        @Test
        void shouldUpdate() {
            var request = userReq.build();
            var user = UserBuilder.aUser().withId(request.id()).build();
            doReturn(Optional.of(user)).when(rep).findById(request.id());
            service.update(request);
            assertAll(
                    () -> assertThat(request.id()).isEqualTo(user.getId()),
                    () -> assertThat(request.firstname()).isEqualTo(user.getFirstname()),
                    () -> assertThat(request.lastname()).isEqualTo(user.getLastname()));
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
            var user = UserBuilder.aUser().withId(id).build();
            doReturn(Optional.of(user)).when(rep).findById(id);
            assertAll(() -> assertDoesNotThrow(() -> service.delete(id)),
                    () -> verify(rep).delete(user));
        }

    }

}