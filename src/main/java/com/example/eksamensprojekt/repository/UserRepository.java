package com.example.eksamensprojekt.repository;
import com.example.eksamensprojekt.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("eksamensprojekt_DB")
public class UserRepository implements IUserRepository {

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT * FROM user";
            Statement stmt = connection.createStatement();
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

    @Override
    public User getUser(int userid) {
        User user = null;
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT * FROM user WHERE userid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("userid");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                user = new User(id, name, username, password);
            }
            return user;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getUserid(String username) {
        int userId = 0;
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "select userid from user where username = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("userid");
            }
            return userId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean usernameExists(String username) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT COUNT(*) FROM user WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
        return false;
    }

    @Override
    public void registerUser(User user) {
        try {
            if (usernameExists(user.getUserName())) {
                throw new IllegalArgumentException("Username already exists");
            }
            Connection connection = ConnectionDB.connection();
            String SQL = "INSERT INTO user (name, username, password) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, user.getname());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(int userid) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "SELECT userid FROM user WHERE userid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userid = rs.getInt("userid");
            }
            String SQL2 = "DELETE FROM userproject WHERE userid=?";
            PreparedStatement ps2 = connection.prepareStatement(SQL2);
            ps2.setInt(1,userid);
            ps2.executeUpdate();

            SQL = "DELETE FROM user WHERE userid = ?";
            ps = connection.prepareStatement(SQL);
            ps.setInt(1, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addUserToProject(int userid, int projectid) {
        try {
            Connection connection = ConnectionDB.connection();
            String checkSQL = "SELECT * FROM userproject WHERE userid = ? AND projectid = ?";
            PreparedStatement checkPs = connection.prepareStatement(checkSQL);
            checkPs.setInt(1, userid);
            checkPs.setInt(2, projectid);
            ResultSet rs = checkPs.executeQuery();
            if(rs.next()) {
                System.out.println("User has already been assigned");
                return;
            }

            String SQL = "INSERT INTO userproject (userid, projectid) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, userid);
            ps.setInt(2, projectid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserFromProject(int projectid, int userid){
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "DELETE FROM userproject WHERE projectid=? AND userid=?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, projectid);
            ps.setInt(2, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateName(int userid, String name) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "UPDATE user SET name = ? WHERE userid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, name);
            ps.setInt(2, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUsername(int userid, String username) {
        if (usernameExists(username) == false) {
            try {
                Connection connection = ConnectionDB.connection();
                String SQL = "UPDATE user SET username = ? WHERE userid = ?";
                PreparedStatement ps = connection.prepareStatement(SQL);
                ps.setString(1, username);
                ps.setInt(2, userid);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updatePassword(int userid, String password) {
        try {
            Connection connection = ConnectionDB.connection();
            String SQL = "UPDATE user SET password = ? WHERE userid = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, password);
            ps.setInt(2, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
