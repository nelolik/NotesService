package ru.nelolik.studingspring.NotesService.config;

import com.zaxxer.hikari.HikariConfig;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;

@TestConfiguration
@ComponentScan("ru.nelolik.studingspring.NotesService.db")
public class TestContainerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public JdbcDatabaseContainer<?> jdbcDatabaseContainer() {
        return new PostgreSQLContainer<>("postgres:14.1")
                .withInitScript("testContainerData.sql")
                .waitingFor(Wait.forListeningPort());
    }

    @Bean
    public org.hibernate.cfg.Configuration testContainerConfigurationForTC(JdbcDatabaseContainer<?> jdbcDatabaseContainer) {
        Logger log = LoggerFactory.getLogger(TestContainerConfig.class);

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcDatabaseContainer.getJdbcUrl());
        hikariConfig.setUsername(jdbcDatabaseContainer.getUsername());
        hikariConfig.setPassword(jdbcDatabaseContainer.getPassword());

        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Note.class);
        configuration.addAnnotatedClass(UserRole.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", jdbcDatabaseContainer.getDriverClassName());
        configuration.setProperty("hibernate.connection.url", jdbcDatabaseContainer.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", jdbcDatabaseContainer.getUsername());
        configuration.setProperty("hibernate.connection.password", jdbcDatabaseContainer.getPassword());
        configuration.setProperty("hibernate.ddl-auto", "none");
        configuration.setProperty("hibernate.show_sql", "true");

        log.info("First mapped port: " + jdbcDatabaseContainer.getFirstMappedPort());

        return configuration;
    }

    @Bean
    public static SessionFactory createTestContainerSessionFactoryForTC(org.hibernate.cfg.Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
