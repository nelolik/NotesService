package ru.nelolik.studingspring.NotesService.config;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

@Configuration
@ComponentScan("ru.nelolik.studingspring.NotesService")
public class SpringConfig  implements WebMvcConfigurer {

    private ApplicationContext context;
    private Environment environment;

    public SpringConfig(ApplicationContext context, Environment environment) {
        this.context = context;
        this.environment = environment;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(context);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Bean
    public org.hibernate.cfg.Configuration getHibernateConfig() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Note.class);

        configuration.setProperty("hibernate.dialect", environment.getProperty("dbConnection.dialect"));
        configuration.setProperty("hibernate.connection.driver_class", environment.getProperty("dbConnection.driver"));
        configuration.setProperty("hibernate.connection.url", environment.getProperty("dbConnection.url"));
        configuration.setProperty("hibernate.connection.username", environment.getProperty("dbConnection.username"));
        configuration.setProperty("hibernate.connection.password", environment.getProperty("dbConnection.password"));
        configuration.setProperty("hibernate.show_sql", environment.getProperty("dbConnection.showSql"));
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
