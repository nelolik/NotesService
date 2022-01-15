package ru.nelolik.studingspring.NotesService.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO_Hibernate;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO_Hibernate;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDao;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.service.NotesService;
import ru.nelolik.studingspring.NotesService.db.service.NotesServiceImpl;
import ru.nelolik.studingspring.NotesService.db.service.UserServiceImpl;
import ru.nelolik.studingspring.NotesService.db.service.UsersService;

@Configuration
public class TestConfig {

    @Bean public UsersDao usersDao() {
        return new UsersDAO_Hibernate(createSessionFactory(getH2Configuration()));
    }

    @Bean
    public UsersService usersService() {
        return new UserServiceImpl(usersDao());
    }

    @Bean
    public NotesDAO notesDAO() {
        return new NotesDAO_Hibernate(createSessionFactory(getH2Configuration()));
    }

    @Bean
    public NotesService notesService() {
        return new NotesServiceImpl(notesDAO());
    }

    @Bean
    public org.hibernate.cfg.Configuration getH2Configuration() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Note.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./h2db");
        configuration.setProperty("hibernate.connection.username", "test");
        configuration.setProperty("hibernate.connection.password", "test");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.show_sql", "true");
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
