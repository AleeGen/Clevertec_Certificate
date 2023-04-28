package ru.clevertec.ecl.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.clevertec.ecl.TestContainer;
import ru.clevertec.ecl.builder.impl.GCBuilder;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateRepositoryTest extends TestContainer {

    private static SessionFactory sessionFactory;
    private GiftCertificateRepository rep;

    @BeforeAll
    static void init() {
        sessionFactory = sessionFactory();
    }

    @BeforeEach
    void setUp() {
        rep = new GiftCertificateRepository(sessionFactory);
        sessionFactory.getCurrentSession().beginTransaction();
    }

    @AfterEach
    void tearDown() {
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    @Nested
    class CheckFind extends TestContainer {

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

    }

    @Nested
    class CheckUpdateSaveDelete {

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