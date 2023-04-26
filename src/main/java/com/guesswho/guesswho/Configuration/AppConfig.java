package com.guesswho.guesswho.Configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class AppConfig {

    @Bean
    //Base de datos local
   /* public DataSource dataSource() {
       DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://localhost:58214;databaseName=GuessWho");
        dataSource.setUsername("usersql");
        dataSource.setPassword("usersql");
        return dataSource;
    }
    */ 
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://containers-us-west-89.railway.app:5922/railway");
        dataSource.setUsername("root");
        dataSource.setPassword("1c3S9nOlE1DfOpZdPJ9H");
        return dataSource;
    }
}