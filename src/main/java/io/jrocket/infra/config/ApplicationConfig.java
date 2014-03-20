package io.jrocket.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static io.jrocket.infra.util.PropertyHelper.setProperty;

@Configuration
@ComponentScan(basePackages = {"io.jrocket"})
@PropertySource("classpath:application.properties")
@EnableScheduling
@EnableTransactionManagement
@EnableJpaRepositories("io.jrocket.infra.repository")
public class ApplicationConfig {

    @Inject
    Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(env.getProperty("hibernate.connection.driver_class"));
        ds.setUrl(env.getProperty("hibernate.connection.url"));
        ds.setUsername(env.getProperty("hibernate.connection.username"));
        ds.setPassword(env.getProperty("hibernate.connection.password"));
        return ds;
    }

    @Bean
    public Properties jpaProperties() {
        Properties properties = new Properties();

        // JDBC connection
        setProperty(env, properties, "hibernate.connection.driver_class");
        setProperty(env, properties, "hibernate.connection.url");
        setProperty(env, properties, "hibernate.connection.username");
        setProperty(env, properties, "hibernate.connection.password");

        // Hibernate properties
        setProperty(env, properties, "hibernate.dialect");
        setProperty(env, properties, "hibernate.show_sql");
        setProperty(env, properties, "hibernate.hbm2ddl.auto");
        setProperty(env, properties, "hibernate.generate_statistics");
        setProperty(env, properties, "hibernate.archive.autodetection");
        setProperty(env, properties, "hibernate.use_sql_comments");
        setProperty(env, properties, "hibernate.format_sql");

        return properties;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setDataSource(dataSource());
        factory.setJpaProperties(jpaProperties());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory());
        return transactionManager;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

}
