package com.gb.lesson_9.sevices.persistence;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfiguration {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Bean
    public TemperatureRepository getTemperatureRepository() {
        return new TempTemperatureRepository(jdbcTemplate);
    }

}
