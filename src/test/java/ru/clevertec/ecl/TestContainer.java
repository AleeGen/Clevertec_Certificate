package ru.clevertec.ecl;

import org.hibernate.SessionFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Testcontainers
public class TestContainer {

    @Container
    private static final PostgreSQLContainer<?> postgres;
    private static final Properties properties;

    static {
        postgres = new PostgreSQLContainer<>("postgres:15.1")
                .withInitScript("script.sql");
        postgres.start();
        properties = new Properties();
        try {
            properties.load(new FileReader(Objects.requireNonNull(
                    TestContainer.class.getClassLoader().getResource("application.properties")).getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        properties.put("hibernate.connection.url", postgres.getJdbcUrl());
        properties.put("hibernate.connection.username", postgres.getUsername());
        properties.put("hibernate.connection.password", postgres.getPassword());
    }

    public static SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration()
                .setProperties(properties)
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(GiftCertificate.class)
                .buildSessionFactory();
    }

}