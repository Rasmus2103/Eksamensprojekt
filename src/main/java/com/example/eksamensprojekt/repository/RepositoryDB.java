package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Date;
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
        try {
            String SQL = "SELECT userid FROM user WHERE userid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userid = rs.getInt("userid");
            }

            SQL = "DELETE FROM user WHERE userid = ?";
            ps = connection().prepareStatement(SQL);
            ps.setInt(1, userid);
            ps.executeQuery();

        } catch (SQLException e) {
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
        try {
            String SQL = "SELECT p.projectname, p.projectid FROM project p " +
                    "JOIN userproject up ON p.projectid = up.projectid " +
                    "WHERE up.userid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String projectname = rs.getString("projectname");
                int projectid = rs.getInt("projectid");
                projects.add(new Project(projectid, projectname));
            }
            return projects;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Project getSpecificProject(int projectid) {
        try {
            String SQL = "SELECT projectname, projectid FROM project WHERE projectid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            Project project = null;
            while (rs.next()) {
                String projectname = rs.getString("projectname");
                projectid = rs.getInt("projectid");
                project = new Project(projectid, projectname);
            }
            return project;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addProject(int userid, String projectname) {
        try {
            String SQL = "INSERT INTO project (projectname) VALUES (?)";
            PreparedStatement ps = connection().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, projectname);
            ps.executeQuery();

            int projectid = getProjectId(projectname);
            addUserToProject(userid, projectid);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addUserToProject(int userid, int projectid) {
        try {
            String SQL = "INSERT INTO userproject (userid, projectid) VALUES (?,?)";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, userid);
            ps.setInt(2, projectid);
            ps.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getProjectId(String projectname) {
        try {
            int projectid = 0;
            String SQL = "SELECT projectid FROM project WHERE projectname = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            projectid = rs.getInt("projectid");
            return projectid;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteProject(int projectid) {
        //TODO
    }

    @Override
    public void updateProjectName(int projectid, String projectname) {
        try {
            String SQL = "update project set projectname = ? where projectid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setString(1, projectname);
            ps.setInt(2, projectid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Board> getBoards(int projectid) {
        List<Board> boards = new ArrayList<>();
        try{
            String SQL = "SELECT * FROM board WHERE projectid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String boardname = rs.getString("boardname");
                int boardid = rs.getInt("boardid");
                boards.add(new Board(boardid, boardname, projectid));
            }
            return boards;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public Board getSpecificBoard(int boardid) {
        try{
            String SQL = "SELECT boardname, boardid, projectid FROM board WHERE boardid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, boardid);
            ResultSet rs = ps.executeQuery();
            Board board = null;
            while(rs.next()){
                String boardname = rs.getString("boardname");
                int projectid = rs.getInt("projectid");
                boardid = rs.getInt("boardid");
                board = new Board(boardid, boardname, projectid);
            }
            return board;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void addBoard(int projectid, String boardname) {
        try{
            String SQL = "INSERT INTO board (boardname, projectid) VALUES (?,?)";
            PreparedStatement ps = connection().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, boardname);
            ps.setInt(2, projectid);
            ps.executeQuery();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteBoard(int boardid) {
        //TODO metode ikke færdig
        try {
            String SQL = "SELECT boardid FROM board WHERE boardid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, boardid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boardid = rs.getInt("boardid");
            }

            SQL = "DELETE FROM story WHERE boardid = ?";
            ps = connection().prepareStatement(SQL);
            ps.setInt(1, boardid);
            ps.executeUpdate();

            SQL = "DELETE FROM board WHERE boardid = ?";
            ps = connection().prepareStatement(SQL);
            ps.setInt(1, boardid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateBoardName(int boardid, String boardname) {
        try {
            String SQL = "update board set boardname = ? where boardid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setString(1, boardname);
            ps.setInt(2, boardid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Story> getStories(int boardid) {
        List<Story> stories = new ArrayList<>();
        try{
            String SQL = "SELECT * FROM story WHERE boardid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, boardid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int storyid = rs.getInt("storyid");
                String storyname = rs.getString("storyname");
                String storydescription = rs.getString("storydescription");
                String acceptcriteria = rs.getString("acceptcriteria");
                Date deadline = rs.getDate("deadline");
                stories.add(new Story(storyid, storyname, storydescription, acceptcriteria, deadline, boardid));
            }
            return stories;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Story getSpecificStory(int storyid) {
        try{
            String SQL = "SELECT storyid, storyname, storydescription, acceptcriteria, deadline, boardid FROM story WHERE storyid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, storyid);
            ResultSet rs = ps.executeQuery();
            Story story = null;
            while(rs.next()){
                storyid = rs.getInt("storyid");
                String storyname = rs.getString("storyname");
                String storydescription = rs.getString("storydescription");
                String acceptcriteria = rs.getString("acceptcriteria");
                Date deadline = rs.getDate("deadline");
                int boardid = rs.getInt("boardid");

                story = new Story(storyid, storyname, storydescription, acceptcriteria, deadline, boardid);
            }
            return story;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addStory(int boardid, Story story) {
        try{
            String SQL = "INSERT INTO story (storyname, storydescription, acceptcriteria, deadline, boardid) VALUES (?,?,?,?,?)";
            PreparedStatement ps = connection().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, story.getStoryname());
            ps.setString(2, story.getStorydescription());
            ps.setString(3, story.getAcceptcriteria());
            ps.setDate(4, story.getDeadline());
            ps.setInt(5, boardid);
            ps.executeQuery();

        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteStory(int storyid) {
        try {
            String SQL = "SELECT storyid FROM story WHERE storyid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, storyid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                storyid = rs.getInt("storyid");
            }

            SQL = "DELETE FROM task WHERE storyid = ?";
            ps = connection().prepareStatement(SQL);
            ps.setInt(1, storyid);
            ps.executeUpdate();

            SQL = "DELETE FROM story WHERE storyid = ?";
            ps = connection().prepareStatement(SQL);
            ps.setInt(1, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStoryName(int storyid, String storyname) {
        try {
            String SQL = "update story set storyname = ? where storyid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setString(1, storyname);
            ps.setInt(2, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStoryDescription(int storyid, String storydescription) {
        try {
            String SQL = "update story set storydescription = ? where storyid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setString(1, storydescription);
            ps.setInt(2, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStoryAcceptcriteria(int storyid, String storyacceptcriteria) {
        try {
            String SQL = "update story set acceptcriteria = ? where storyid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setString(1, storyacceptcriteria);
            ps.setInt(2, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStoryDeadline(int storyid, Date storydeadline) {
        try {
            String SQL = "update story set deadline = ? where storyid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setDate(1, (java.sql.Date) storydeadline);
            ps.setInt(2, storyid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Task> getTasks(int storyid) {
        List<Task> tasks = new ArrayList<>();
        try{
            String SQL = "SELECT * FROM task WHERE storyid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, storyid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int taskid = rs.getInt("taskid");
                String taskname = rs.getString("taskname");
                String taskdescription = rs.getString("taskdescription");
                int storypoints = rs.getInt("storypoints");
                tasks.add(new Task(taskid, taskname, taskdescription, storypoints, storyid));
            }
            return tasks;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
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
        try {
            String SQL = "SELECT taskid FROM task where taskid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, taskid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                taskid = rs.getInt("taskid");
            }
            SQL = "DELETE FROM task WHERE taskid = ?";
            ps = connection().prepareStatement(SQL);
            ps.setInt(1, taskid);
            ps.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTaskName(int taskid, String taskname) {
        try {
            String SQL = "update task set taskname = ? where taskid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setString(1, taskname);
            ps.setInt(2, taskid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTaskDescription(int taskid, String taskdescription) {
        try {
            String SQL = "update task set taskdescription = ? where taskid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setString(1, taskdescription);
            ps.setInt(2, taskid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTaskStorypoints(int taskid, int storypoints) {
        try {
            String SQL = "update task set storypoints = ? where taskid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, storypoints);
            ps.setInt(2, taskid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


}