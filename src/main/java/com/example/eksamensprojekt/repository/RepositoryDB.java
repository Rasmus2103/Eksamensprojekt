package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository("eksamensprojekt_DB")
public class RepositoryDB implements IRepositoryDB {

    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String uid;

    @Value("${spring.datasource.password}")
    private String pwd;

    public Connection connection() {
        try {
            Connection con = DriverManager.getConnection(db_url, uid, pwd);
            return con;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUser(int userid) {
        User user = null;
        try {
            String SQL = "SELECT * FROM user WHERE userid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
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
            String SQL = "select userid from user where username = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
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
            String SQL = "SELECT COUNT(*) FROM user WHERE username = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
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
            if (usernameExists(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            String SQL = "INSERT INTO user (name, username, password) VALUES (?, ?, ?)";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(int userid) {

    }

    @Override
    public void updateUser(User user) {

    }


    @Override
    public List<Project> getProjects(int userid) {
        return null;
    }

    @Override
    public Project getSpecificProject(int projectid) {
        return null;
    }

    @Override
    public void addProject(int userid, String projectname) {

    }

    @Override
    public void deleteProject(int projectid) {

    }

    @Override
    public void updateProject(Project project) {

    }


    @Override
    public List<board> getBoards(int projectid) {
        return null;
    }

    @Override
    public board getSpecificBoard(int boardid) {
        return null;
    }

    @Override
    public void addBoard(int projectid, String boardname) {

    }

    @Override
    public void deleteBoard(int boardid) {

    }

    @Override
    public void updateBoard(board board) {

    }


    @Override
    public List<Story> getStories(int boardid) {
        return null;
    }

    @Override
    public Story getSpecificStory(int storyid) {
        return null;
    }

    @Override
    public void addStory(int boardid, String storyname) {

    }

    @Override
    public void deleteStory(int storyid) {

    }

    @Override
    public void updateStory(Story story) {

    }


    @Override
    public List<Task> getTasks(int storyid) {
        return null;
    }

    @Override
    public Task getSpecificTask(int taskid) {
        return null;
    }

    @Override
    public void addTask(int storyid, String taskname) {

    }

    @Override
    public void deleteTask(int taskid) {

    }

    @Override
    public void updateTask(Task task) {

    }


}