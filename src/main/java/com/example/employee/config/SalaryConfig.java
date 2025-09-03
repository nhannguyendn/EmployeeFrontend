package com.example.employee.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.employee.salary.repository", entityManagerFactoryRef = "salaryEntityManagerFactory", transactionManagerRef = "salaryTransactionManager")
public class SalaryConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.salary")
    public DataSourceProperties salaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource salaryDataSource() {
        return salaryDataSourceProperties().initializeDataSourceBuilder().build();
    }

    // @Bean
    // @ConfigurationProperties(prefix = "spring.datasource.salary")
    // public DataSource salaryDataSource() {
    // return DataSourceBuilder.create().build();
    // }

    @Bean(name = "salaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean salaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(salaryDataSource())
                .packages("com.example.employee.salary.model")
                .persistenceUnit("salaryPU")
                .properties(Map.of(
                        "hibernate.hbm2ddl.auto", "update", // migrate attendance_db
                        "hibernate.dialect", "org.hibernate.dialect.MySQLDialect"))
                .build();
    }

    @Bean(name = "salaryTransactionManager")
    public PlatformTransactionManager salaryTransactionManager(
            @Qualifier("salaryEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}
