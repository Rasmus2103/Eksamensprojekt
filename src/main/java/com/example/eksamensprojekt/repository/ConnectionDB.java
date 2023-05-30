package com.example.eksamensprojekt.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionDB {

    private static Connection connection;
    private static String Url;
    private static String Uid;
    private static String Pwd;

    @Value("${spring.datasource.url}")
    public void url(String url) {
        Url = url;
    }

    @Value("${spring.datasource.username}")
    public void setUid(String uid) {
        Uid = uid;
    }

    @Value("${spring.datasource.password}")
    public void setPwd(String pwd) {
        Pwd = pwd;
    }

    public static Connection connection() {
        try {
            if (connection == null) connection = DriverManager.getConnection(Url, Uid, Pwd);
            return connection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
