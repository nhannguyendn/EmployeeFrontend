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
@EnableJpaRepositories(basePackages = "com.example.employee.attendance.repository", entityManagerFactoryRef = "attendanceEntityManagerFactory", transactionManagerRef = "attendanceTransactionManager")
public class AttendanceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.attendance")
    public DataSourceProperties attendanceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource attendanceDataSource() {
        return attendanceDataSourceProperties().initializeDataSourceBuilder().build();
    }

    // @Bean
    // @ConfigurationProperties(prefix = "spring.datasource.attendance")
    // public DataSource attendanceDataSource() {
    // return DataSourceBuilder.create().build();
    // }

    @Bean(name = "attendanceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean attendanceEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(attendanceDataSource())
                .packages("com.example.employee.attendance.model")
                .persistenceUnit("attendance")
                .properties(Map.of(
                        "hibernate.hbm2ddl.auto", "update", // migrate attendance_db
                        "hibernate.dialect", "org.hibernate.dialect.MySQLDialect"))
                .build();
    }

    @Bean(name = "attendanceTransactionManager")
    public PlatformTransactionManager attendanceTransactionManager(
            @Qualifier("attendanceEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}
