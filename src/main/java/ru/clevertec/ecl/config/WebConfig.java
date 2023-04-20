package ru.clevertec.ecl.config;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;

import java.util.Properties;

@RequiredArgsConstructor
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "ru.clevertec.ecl")
@PropertySource(value = "classpath:application.properties")
public class WebConfig implements WebMvcConfigurer {

    private final Environment env;

    @Bean
    public SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration()
                .setProperties(properties())
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(GiftCertificate.class)
                .buildSessionFactory();
    }

    private Properties properties() {
        Properties properties = new Properties();
        properties.put("hibernate.connection.url", env.getProperty("hibernate.connection.url"));
        properties.put("hibernate.default_schema", env.getProperty("hibernate.default_schema"));
        properties.put("hibernate.connection.username", env.getProperty("hibernate.connection.username"));
        properties.put("hibernate.connection.password", env.getProperty("hibernate.connection.password"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        properties.put("hibernate.connection.driver_class", env.getProperty("hibernate.connection.driver_class"));
        properties.put("hibernate.current_session_context_class", env.getProperty("hibernate.current_session_context_class"));
        return properties;
    }

}