package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String uid;

    @Value("${spring.datasource.password}")
    private String pwd;

    public Connection connection() {
        try {
            Connection con = DriverManager.getConnection(db_url,uid,pwd);
            return con;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            String SQL = "SELECT * FROM user";
            Statement stmt = connection().createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while(rs.next()) {
                int id = rs.getInt("userid");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                users.add(new User(id, name, username, password));
            }
            return users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
