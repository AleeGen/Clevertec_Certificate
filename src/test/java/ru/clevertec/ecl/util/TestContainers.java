package ru.clevertec.ecl.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

@UtilityClass
public class TestContainers {

    public static SessionFactory getSessionFactory(PostgreSQLContainer<?> postgres) {
        return new org.hibernate.cfg.Configuration()
                .setProperty("hibernate.connection.url", postgres.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgres.getUsername())
                .setProperty("hibernate.connection.password", postgres.getPassword())
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "true")
                .setProperty("hibernate.default_schema", "custom")
                .setProperty("hibernate.current_session_context_class", "thread")
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(GiftCertificate.class)
                .buildSessionFactory();
    }

    public static PostgreSQLContainer<?> getContainer() {
        return new PostgreSQLContainer<>("postgres:15.1").withInitScript("script.sql");
    }

}