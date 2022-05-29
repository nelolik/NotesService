package ru.nelolik.studingspring.NotesService.config;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;

@Configuration
@EnableJpaRepositories("ru.nelolik.studingspring.NotesService.db")
@Slf4j
@EnableCaching
public class SpringConfig implements WebMvcConfigurer {

    @Autowired
    private Environment environment;

    @Bean
    public SpringTemplateEngine templateEngine(ApplicationContext context) {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(context);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Bean
    public org.hibernate.cfg.Configuration hibernateConfig() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Note.class);
        configuration.addAnnotatedClass(UserRole.class);

        configuration.setProperty("hibernate.dialect", environment.getProperty("dbConnection.dialect"));
        configuration.setProperty("hibernate.connection.driver_class", environment.getProperty("dbConnection.driver"));
        configuration.setProperty("hibernate.connection.url", environment.getProperty("dbConnection.url"));
        configuration.setProperty("hibernate.connection.username", environment.getProperty("dbConnection.username"));
        configuration.setProperty("hibernate.connection.password", environment.getProperty("dbConnection.password"));
        configuration.setProperty("hibernate.show_sql", environment.getProperty("dbConnection.showSql"));
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        return configuration;
    }

    @Bean
    public SessionFactory createSessionFactory(org.hibernate.cfg.Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CacheNames.ALL_USERS,
                CacheNames.USER,
                CacheNames.NOTES);
    }

}
