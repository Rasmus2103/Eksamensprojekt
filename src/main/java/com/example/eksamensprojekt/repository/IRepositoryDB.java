package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.*;

import java.util.List;

public interface IRepositoryDB {
    User getUser(int userid);
    int getUserid(String username);
    boolean usernameExists(String username);
    void registerUser(User user);
    void deleteUser(int userid);
    void updateUser(User user);

    List<Project> getProjects(int userid);
    Project getSpecificProject(int projectid);
    void addProject(int userid, String projectname);
    void deleteProject(int projectid);
    void updateProject(Project project);

     List<board> getBoards(int projectid);
    board getSpecificBoard(int boardid);
    void addBoard(int projectid, String boardname);
    void deleteBoard(int boardid);
    void updateBoard(board board);

    List<Story> getStories(int boardid);
    Story getSpecificStory(int storyid);
    void addStory(int boardid, String storyname);
    void deleteStory(int storyid);
    void updateStory(Story story);

    List<Task> getTasks(int storyid);
    Task getSpecificTask(int taskid);
    void addTask(int storyid, String taskname);
    void deleteTask(int taskid);
    void updateTask(Task task);

}
