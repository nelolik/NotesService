package ru.nelolik.studingspring.NotesService.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO_ImplementedWithHibernate;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO_ImplementedWithHibernate;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import static org.mockito.Mockito.mock;

@TestConfiguration
@ComponentScan("test.ru.nelolik.studingspring.NotesService")
public class TestConfig {

    @Bean
    public UsersDAO usersDao() {
        return new UsersDAO_ImplementedWithHibernate(createSessionFactory(getH2Configuration()));
    }

    @Bean
    public NotesDAO notesDAO() {
        return new NotesDAO_ImplementedWithHibernate(createSessionFactory(getH2Configuration()));
    }

    public org.hibernate.cfg.Configuration getH2Configuration() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Note.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:h2db");
        configuration.setProperty("hibernate.connection.username", "test");
        configuration.setProperty("hibernate.connection.password", "test");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("spring.h2.console.enabled", "true");
        return configuration;
    }

    @Bean
    public static SessionFactory createSessionFactory(org.hibernate.cfg.Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
