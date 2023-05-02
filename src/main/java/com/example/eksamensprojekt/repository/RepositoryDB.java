package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
        return null;
    }

    @Override
    public int getUserid(String username) {
        return 0;
    }

    @Override
    public boolean usernameExists(String username) {
        return false;
    }

    @Override
    public void registerUser(User user) {

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