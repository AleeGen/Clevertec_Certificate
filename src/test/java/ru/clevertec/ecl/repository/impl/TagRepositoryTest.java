package ru.clevertec.ecl.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.clevertec.ecl.TestContainer;
import ru.clevertec.ecl.builder.impl.TagBuilder;
import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryTest extends TestContainer {

    private static SessionFactory sessionFactory;
    private TagRepository rep;

    @BeforeAll
    static void init() {
        sessionFactory = sessionFactory();
    }

    @BeforeEach
    void setUp() {
        rep = new TagRepository(sessionFactory);
        sessionFactory.getCurrentSession().beginTransaction();
    }

    @AfterEach
    void tearDown() {
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    @Nested
    class CheckFind {

        @Test
        void allShouldReturnExpectedSize() {
            int expected = 10;
            int actual = rep.findAll().size();
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

    }

    @Nested
    class CheckUpdateSave {

        @Test
        void shouldUpdate() {
            long id = 1L;
            var actual = TagBuilder.aTag().withId(id).build();
            rep.update(actual);
            var expected = rep.findById(id).get();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldSave() {
            var request = TagBuilder.aTag().withId(null).withName("newName").build();
            var response = rep.save(request);
            assertThat(response.getId()).isNotNull();
        }

        @Test
        void shouldDelete() {
            long id = 2L;
            var before = rep.findById(id).get();
            rep.delete(before);
            Optional<Tag> after = rep.findById(id);
            assertThat(after).isEmpty();
        }

    }

}