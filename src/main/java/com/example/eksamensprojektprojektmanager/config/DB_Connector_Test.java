package com.example.eksamensprojektprojektmanager.config;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@Profile("test")
public class DB_Connector_Test {
    private static Connection connection = null;

    private static String getProperty(String property_name) {
        Properties properties = new Properties();
        try (InputStream input = DB_Connector_Test.class.getClassLoader().getResourceAsStream("application-test.properties")) {
            properties.load(input);

        } catch (IOException e) {
            System.out.println("Failed to load application properties");
            e.printStackTrace();
        }
        return properties.getProperty(property_name);
    }

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        String dbUrl = getProperty("spring.datasource.url");
        String uid = getProperty("spring.datasource.username");
        String pwd = getProperty("spring.datasource.password");

        try {
            connection = DriverManager.getConnection(dbUrl, uid, pwd);
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }
}