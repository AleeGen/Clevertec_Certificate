package ru.clevertec.ecl.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.ecl.util.TestContainers;
import ru.clevertec.ecl.util.builder.impl.GCBuilder;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.util.builder.impl.TagBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateRepositoryTest {

    private static SessionFactory sessionFactory;
    private static GiftCertificateRepository rep;

    @BeforeEach
    void setUp() {
        sessionFactory.getCurrentSession().beginTransaction();
    }

    @AfterEach
    void tearDown() {
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    @Testcontainers
    @Nested
    class CheckFind {

        @Container
        public static PostgreSQLContainer<?> postgres = TestContainers.getContainer();

        private static Stream<Arguments> argsByTagName() {
            return Stream.of(
                    Arguments.of("n1", false, new String[]{"name", "duration"}, List.of(1L, 5L)),
                    Arguments.of("n3", false, new String[]{"description"}, List.of(2L, 5L)),
                    Arguments.of("n4", true, new String[]{"price"}, List.of(5L, 3L)));
        }

        private static Stream<Arguments> argsByPart() {
            return Stream.of(
                    Arguments.of("n", true, new String[]{"name", "duration"}, List.of(5L, 4L, 3L, 2L, 1L)),
                    Arguments.of("d", false, new String[]{"description"}, List.of(1L, 2L, 3L, 4L, 5L)),
                    Arguments.of("2", true, new String[]{"price"}, List.of(2L)),
                    Arguments.of("n5", true, new String[]{"price"}, List.of(5L)));
        }

        @BeforeAll
        static void init() {
            sessionFactory = TestContainers.getSessionFactory(postgres);
            rep = new GiftCertificateRepository(sessionFactory);
        }

        @Test
        void allShouldReturnExpectedSize() {
            int expected = 5;
            int actual = rep.findAll().size();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void allShouldReturnWithTags() {
            long expected = 15;
            List<GiftCertificate> all = rep.findAll();
            long actual = all.stream().mapToLong(o -> o.getTags().size()).sum();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void byIdShouldReturnEmpty() {
            assertThat(rep.findById(-1L)).isEmpty();
        }

        @Test
        void byIdShouldReturnExpected() {
            long id = 1L;
            var response = rep.findById(id).get();
            assertAll(
                    () -> assertThat(response).isNotNull(),
                    () -> assertThat(response.getId()).isEqualTo(id));
        }

        @Test
        void byIdShouldReturnWithTags() {
            int expected = 5;
            int actual = rep.findById(5L).get().getTags().size();
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @MethodSource("argsByTagName")
        void byTagNameShouldReturnExpected(String tagName, boolean isDesc, String[] bySort, List<Long> expected) {
            List<GiftCertificate> response = rep.findByTagName(tagName, isDesc, bySort);
            List<Long> actual = response.stream().map(GiftCertificate::getId).toList();
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @MethodSource("argsByPart")
        void byPartShouldReturnExpected(String part, boolean isDesc, String[] bySort, List<Long> expected) {
            List<GiftCertificate> response = rep.findByPart(part, isDesc, bySort);
            List<Long> actual = response.stream().map(GiftCertificate::getId).toList();
            assertThat(actual).isEqualTo(expected);
        }

    }

    @Testcontainers
    @Nested
    class CheckUpdateSaveDelete {

        @Container
        public static PostgreSQLContainer<?> postgres = TestContainers.getContainer();

        @BeforeAll
        static void init() {
            sessionFactory = TestContainers.getSessionFactory(postgres);
            rep = new GiftCertificateRepository(sessionFactory);
        }

        @Test
        void shouldUpdate() {
            long id = 1L;
            var actual = GCBuilder.aGC().withId(id).build();
            rep.update(actual);
            var expected = rep.findById(id).get();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldSave() {
            var request = GCBuilder.aGC().withId(null).withTags(Collections.emptyList()).build();
            var response = rep.save(request);
            assertThat(response.getId()).isNotNull();
        }

        @Test
        void shouldDelete() {
            long id = 2L;
            var before = rep.findById(id).get();
            rep.delete(before);
            Optional<GiftCertificate> after = rep.findById(id);
            assertThat(after).isEmpty();
        }

    }

}