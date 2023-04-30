package ru.clevertec.ecl.repository.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.ecl.util.TestContainers;
import ru.clevertec.ecl.util.builder.impl.TagBuilder;
import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryTest {

    private static SessionFactory sessionFactory;
    private static TagRepository rep;

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

        @BeforeAll
        static void init() {
            sessionFactory = TestContainers.getSessionFactory(postgres);
            rep = new TagRepository(sessionFactory);
        }

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

        @Test
        void byNameShouldReturnExpected() {
            String name = "n1";
            Tag response = rep.findByName(name);
            assertAll(
                    () -> assertThat(response).isNotNull(),
                    () -> assertThat(response.getName()).isEqualTo(name));
        }

        @Test
        void byNameShouldReturnNull() {
            Tag response = rep.findByName("nonExistentName");
            assertThat(response).isNull();
        }

    }

    @Testcontainers
    @Nested
    class CheckUpdateSave {

        @Container
        public static PostgreSQLContainer<?> postgres = TestContainers.getContainer();

        @BeforeAll
        static void init() {
            sessionFactory = TestContainers.getSessionFactory(postgres);
            rep = new TagRepository(sessionFactory);
        }

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