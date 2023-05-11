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

    public List<User> getAllUsers() {
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
            String SQL2 = "DELETE FROM userproject WHERE userid=?";
            PreparedStatement ps2 = connection().prepareStatement(SQL2);
            ps2.setInt(1,userid);
            ps2.executeUpdate();

            SQL = "DELETE FROM user WHERE userid = ?";
            ps = connection().prepareStatement(SQL);
            ps.setInt(1, userid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserFromProject(int projectid, int userid){
        try {
            String SQL = "DELETE FROM userproject WHERE projectid=? AND userid=?";
            PreparedStatement ps = connection().prepareStatement(SQL);
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
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int projectid = rs.getInt(1);
                addBoard(projectid, "backlog");
                addBoard(projectid, "sprintboard");
                addBoard(projectid, "history board");
                addUserToProject(userid, projectid);
            } else {
                throw new SQLException("Creating project failed, no ID obtained.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void addUserToProject(int userid, int projectid) {
        try {
            String checkSQL = "SELECT * FROM userproject WHERE userid = ? AND projectid = ?";
            PreparedStatement checkPs = connection().prepareStatement(checkSQL);
            checkPs.setInt(1, userid);
            checkPs.setInt(2, projectid);
            ResultSet rs = checkPs.executeQuery();

            if(rs.next()) {
                System.out.println("User has already been assigned");
                return;
            }

            String SQL = "INSERT INTO userproject (userid, projectid) VALUES (?,?)";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, userid);
            ps.setInt(2, projectid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<String> getUserNamesByProjectId(int projectid) {
        List<String> userNames = new ArrayList<>();
        try {
            String SQL = "SELECT u.name " +
                    "FROM user AS u " +
                    "JOIN userproject AS up ON u.userid = up.userid " +
                    "JOIN project AS p ON p.projectid = up.projectid " +
                    "WHERE p.projectid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("name");
                userNames.add(name);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return userNames;
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
        try {
            int boardid;
            String SQL = "SELECT boardid FROM board WHERE projectid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, projectid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                boardid = rs.getInt("boardid");
                deleteBoard(boardid);
            }

            String deleteUserProjectSQL = "DELETE FROM userproject WHERE projectid = ?";
            PreparedStatement psDeleteUserProject = connection().prepareStatement(deleteUserProjectSQL);
            psDeleteUserProject.setInt(1, projectid);
            psDeleteUserProject.executeUpdate();

            SQL = "DELETE FROM project WHERE projectid = ?";
            ps = connection().prepareStatement(SQL);
            ps.setInt(1, projectid);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
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
        //TODO metode skal gøres private så den udelukkende hjælper addproject metoden, brugeren skal ikke selv kunne oprette nogle boards
        try{
            String SQL = "INSERT INTO board (boardname, projectid) VALUES (?,?)";
            PreparedStatement ps = connection().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, boardname);
            ps.setInt(2, projectid);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteBoard(int boardid) {
        //TODO metode skal gøres private så den udelukkende hjælper deleteproject metoden, brugeren skal ikke selv kunne slette nogle boards
        try {
            String SQL = "SELECT boardid FROM board WHERE boardid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, boardid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boardid = rs.getInt("boardid");
            }
            int storyid;
            String SQL2 = "SELECT storyid from story WHERE boardid = ?";
            PreparedStatement ps2 = connection().prepareStatement(SQL2);
            ps2.setInt(1, boardid);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                storyid = rs2.getInt("storyid");
                SQL2 = "DELETE FROM task WHERE storyid = ?";
                ps2 = connection().prepareStatement(SQL2);
                ps2.setInt(1, storyid);
                ps2.executeUpdate();
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
            ps.executeUpdate();
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
        try{
            String SQL = "SELECT taskid, taskname, taskdescription, storypoints, storyid FROM task WHERE taskid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, taskid);
            ResultSet rs = ps.executeQuery();
            Task task = null;
            while(rs.next()){
                taskid = rs.getInt("taskid");
                String taskname = rs.getString("taskname");
                String taskdescription = rs.getString("taskdescription");
                int storypoints = rs.getInt("storypoints");
                int storydid = rs.getInt("storyid");
                task = new Task(taskid, taskname, taskdescription, storypoints, storydid);
            }
            return task;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTask(int storyid, Task task) {
        try{
            String SQL = "INSERT INTO task (taskname, taskdescription, storypoints, storyid) VALUES (?,?,?,?)";
            PreparedStatement ps = connection().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTaskname());
            ps.setString(2, task.getTaskdescription());
            ps.setInt(3, task.getStorypoints());
            ps.setInt(4, storyid);
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
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
            ps.executeUpdate();

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

    public List<Integer> getAllStoryPointsForBoard(int storyid) {
        List<Integer> storyPoints = new ArrayList<>();
        try {
            String SQL = "SELECT storypoints FROM task WHERE storyid = ?";
            PreparedStatement ps = connection().prepareStatement(SQL);
            ps.setInt(1, storyid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                storyPoints.add(rs.getInt("storypoints"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return storyPoints;
    }

    public int getSumOfStoryPointsForBoard(int storyid) {
        List<Integer> storyPoints = getAllStoryPointsForBoard(storyid);
        int sum = 0;
        for (int points : storyPoints) {
            sum += points;
        }
        return sum;
    }



}