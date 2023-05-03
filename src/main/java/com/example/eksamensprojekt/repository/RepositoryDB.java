package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
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
            if (usernameExists(user.getUserName())) {
                throw new IllegalArgumentException("Username already exists");
            }
            String SQL = "INSERT INTO user (name, username, password) VALUES (?, ?, ?)";
            PreparedStatement ps = connection().prepareStatement(SQL);
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
        try{
            String SQL = "SELECT userid FROM user WHERE userid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                userid = rs.getInt("userid");
            }

            SQL = "DELETE FROM user WHERE userid = ?";
            ps = connection().prepareStatement(SQL);
            ps.setInt(1, userid);
            ps.executeQuery();

        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateName(int userid, String name) {
    try {
            String SQL = "UPDATE user SET name = ? WHERE userid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
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
                String SQL = "UPDATE user SET username = ? WHERE userid = ?";
                PreparedStatement ps = connection().prepareStatement(SQL);
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
            String SQL = "UPDATE user SET password = ? WHERE userid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setString(1, password);
            ps.setInt(2, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Project> getProjects(int userid) {
        List<Project> projects = new ArrayList<>();
        try{
            String SQL = "SELECT p.projectname, p.projectid FROM project p " +
                    "JOIN userproject up ON p.projectid = up.projectid " +
                    "WHERE up.userid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String projectname = rs.getString("projectname");
                int projectid = rs.getInt("projectid");
                projects.add(new Project(projectid, projectname));
            }
            return projects;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Project getSpecificProject(int projectid) {
        try{
            String SQL = "SELECT projectname, projectid FROM project WHERE projectid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            Project project = null;
            while(rs.next()){
                String projectname = rs.getString("projectname");
                projectid = rs.getInt("projectid");
                project = new Project(projectid, projectname);
            }
            return project;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addProject(int userid, String projectname) {
        try{
            String SQL = "INSERT INTO project (projectname) VALUES (?)";
            PreparedStatement ps = connection().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, projectname);
            ps.executeQuery();

            int projectid = getProjectId(projectname);
            addUserToProject(userid, projectid);

        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addUserToProject(int userid, int projectid){
        try{
            String SQL = "INSERT INTO userproject (userid, projectid) VALUES (?,?)";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, userid);
            ps.setInt(2, projectid);
            ps.executeQuery();

        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getProjectId(String projectname){
        try{
            int projectid = 0;
            String SQL = "SELECT projectid FROM project WHERE projectname = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            projectid = rs.getInt("projectid");
            return projectid;

        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
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